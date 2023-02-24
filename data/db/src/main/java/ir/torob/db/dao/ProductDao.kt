package ir.torob.db.dao

import ir.torob.data.model.Product
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ProductDao : EntityDao<Product> {

    fun getProductWithKeyFlow(productKey: String): Flow<Product>
    suspend fun getProductWithKey(productKey: String): Product?
    suspend fun delete(id: Long)
    suspend fun delete(productKey: String)
    suspend fun deleteAll()
}

suspend fun ProductDao.insertProductKeyIfNotExist(productKey: String): Long {
    val savedProduct = getProductWithKey(productKey)
    return savedProduct?.id
        ?: upsert(
            Product(
                id = Objects.hash(productKey).toLong(),
                randomKey = productKey,
            )
        )
}

suspend fun ProductDao.insertProductIfNotExist(product: Product): Long {
    val savedProduct = getProductWithKey(product.randomKey!!)
    return savedProduct?.id ?: upsert(product)
}