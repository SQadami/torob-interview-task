package ir.torob.data.product.simular

import ir.torob.data.model.Product
import ir.torob.data.model.SimilarProductEntry
import ir.torob.db.DatabaseTransactionRunner
import ir.torob.db.dao.*
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class SimilarProductStore @Inject constructor(
    similarDao: SimilarDao,
    productDao: ProductDao,
    lastRequestStore: SimilarProductLastRequestStore,
    similarProductDataSource: SimilarProductDataSource,
    transactionRunner: DatabaseTransactionRunner,
) : Store<SimilarProductStoreKey, List<Product>> by StoreBuilder.from(

    fetcher = Fetcher.of { key: SimilarProductStoreKey ->
        similarProductDataSource.getSimilarProducts(
            productKey = key.productKey,
            page = key.page,
            pageSize = key.pageSize,
        ).also {
            lastRequestStore.updateLastRequest(key.productKey)
        }
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { key: SimilarProductStoreKey ->
            similarDao.entriesObservable(key.productKey, key.page).map { entries ->
                when {
                    // Store only treats null as 'no value', so convert to null
                    entries.isEmpty() -> null
                    // If the request is expired, our data is stale
                    lastRequestStore.isRequestExpired(key.productKey, 2.days) -> null
                    // Otherwise, our data is fresh and valid
                    else -> entries.map { it.similar }
                }
            }
        },
        writer = { key, response ->
            transactionRunner {
                productDao.insertProductKeyIfNotExist(key.productKey)
            }

            transactionRunner {
                response.map {
                    productDao.insertProductIfNotExist(it)

                    SimilarProductEntry(
                        id = Objects.hash(key.productKey, it.randomKey).toLong(),
                        productKey = key.productKey,
                        page = key.page,
                        similarKey = it.randomKey!!,
                        index = response.indexOf(it)
                    )
                }.forEach {
                    similarDao.insertIfNotExist(it)
                }
            }
        },
        delete = { key -> similarDao.deletePageByProductKey(key.page, key.productKey) },
        deleteAll = { productDao.deleteAll() },
    ),
).build()

class SimilarProductStoreKey(
    val productKey: String,
    val page: Int,
    val pageSize: Int,
) : Comparable<SimilarProductStoreKey> {

    override fun compareTo(other: SimilarProductStoreKey) = when {
        (productKey == other.productKey && page == other.page && pageSize == other.pageSize) -> 0
        (page > other.page) -> 1
        else -> -1
    }
}