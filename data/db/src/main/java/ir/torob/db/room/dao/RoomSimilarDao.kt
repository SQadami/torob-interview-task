package ir.torob.db.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.data.model.SimilarProductEntry
import ir.torob.db.dao.SimilarDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomSimilarDao : SimilarDao,
    RoomPaginatedEntryDao<SimilarProductEntry, SimilarEntryWithProduct> {

    @Transaction
    @Query("SELECT * FROM similar_products WHERE product_key = :productKey AND similar_key = :similarKey ORDER BY id ASC")
    abstract override fun entry(productKey: String, similarKey: String): SimilarProductEntry

    @Transaction
    @Query("SELECT * FROM similar_products WHERE page = :page AND product_key = :productKey ORDER BY remote_index ASC")
    abstract override fun entriesObservable(
        productKey: String, page: Int
    ): Flow<List<SimilarEntryWithProduct>>

    @Transaction
    @Query("SELECT * FROM similar_products WHERE product_key = :productKey ORDER BY page ASC, remote_index ASC")
    abstract override fun entriesPagingSource(productKey: String): PagingSource<Int, SimilarEntryWithProduct>

    @Query("DELETE FROM similar_products WHERE page = :page AND product_key = :productKey")
    abstract override suspend fun deletePageByProductKey(page: Int, productKey: String)

    @Query("DELETE FROM similar_products WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM similar_products")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from similar_products")
    abstract override suspend fun getLastPage(): Int?
}