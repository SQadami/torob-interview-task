package ir.torob.db.dao

import ir.torob.data.model.EntryWithProduct
import ir.torob.data.model.PaginatedEntry

interface PaginatedEntryDao<EC : PaginatedEntry, LI : EntryWithProduct<EC>> : EntryDao<EC, LI> {

    override suspend fun upsert(entity: EC) : Long
    override suspend fun upsertAll(vararg entity: EC)
    override suspend fun upsertAll(entities: List<EC>)
    suspend fun deletePage(page: Int)
    suspend fun getLastPage(): Int?
}

suspend fun <EC : PaginatedEntry, LI : EntryWithProduct<EC>> PaginatedEntryDao<EC, LI>.updatePage(
    page: Int,
    entities: List<EC>,
) {
    deletePage(page)
    upsertAll(entities)
}