package ir.torob.db.room.dao

import androidx.room.Dao
import androidx.room.Query
import ir.torob.data.model.Product
import ir.torob.db.dao.ProductDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomProductDao : ProductDao, RoomEntityDao<Product> {

    @Query("SELECT * FROM products WHERE random_key = :productKey")
    abstract override fun getProductWithKeyFlow(productKey: String): Flow<Product>

    @Query("SELECT * FROM products WHERE random_key = :productKey")
    abstract override suspend fun getProductWithKey(productKey: String): Product?

    @Query("DELETE FROM products WHERE id = :id")
    abstract override suspend fun delete(id: Long)

    @Query("DELETE FROM products WHERE random_key = :productKey")
    abstract override suspend fun delete(productKey: String)

    @Query("DELETE FROM products")
    abstract override suspend fun deleteAll()
}