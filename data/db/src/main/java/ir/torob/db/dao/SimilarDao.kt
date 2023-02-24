package ir.torob.db.dao

import androidx.paging.PagingSource
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.data.model.SimilarProductEntry
import kotlinx.coroutines.flow.Flow

interface SimilarDao : PaginatedEntryDao<SimilarProductEntry, SimilarEntryWithProduct> {

    fun entry(productKey: String, similarKey: String): SimilarProductEntry?
    fun entriesObservable(productKey: String, page: Int): Flow<List<SimilarEntryWithProduct>>
    fun entriesPagingSource(productKey: String,): PagingSource<Int, SimilarEntryWithProduct>
    suspend fun deletePageByProductKey(page: Int, productKey: String)
    override suspend fun deletePage(page: Int)
    override suspend fun deleteAll()
    override suspend fun getLastPage(): Int?
}

suspend fun SimilarDao.insertIfNotExist(entry: SimilarProductEntry): Long {
    val savedEntry = entry(entry.productKey, entry.similarKey)
    return savedEntry?.id ?: upsert(entry)
}