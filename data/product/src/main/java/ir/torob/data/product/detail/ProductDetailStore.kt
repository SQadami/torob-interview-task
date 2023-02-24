package ir.torob.data.product.detail

import ir.torob.data.model.Product
import ir.torob.db.DatabaseTransactionRunner
import ir.torob.db.dao.ProductDao
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class ProductDetailStore @Inject constructor(
    productDao: ProductDao,
    lastRequestStore: ProductDetailLastRequestStore,
    productDetailDataSource: ProductDetailDataSource,
    transactionRunner: DatabaseTransactionRunner,
) : Store<String, Product> by StoreBuilder.from(

    fetcher = Fetcher.of { productKey: String ->
        val savedProduct = productDao.getProductWithKey(productKey)

        val productResult =
            runCatching { productDetailDataSource.getProductDetail(savedProduct!!.randomKey!!) }
        if (productResult.isSuccess) {
            lastRequestStore.updateLastRequest(productKey)
            return@of productResult.getOrThrow()
        }

        throw productResult.exceptionOrNull()!!
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { productKey ->
            productDao.getProductWithKeyFlow(productKey).map {
                when {
                    // If the request is expired, our data is stale
                    lastRequestStore.isRequestExpired(productKey, 2.days) -> null
                    // Otherwise, our data is fresh and valid
                    else -> it
                }
            }
        },
        writer = { _, response ->
            transactionRunner {
                productDao.upsert(response)
            }
        },
        delete = productDao::delete,
        deleteAll = productDao::deleteAll,
    ),
).build()