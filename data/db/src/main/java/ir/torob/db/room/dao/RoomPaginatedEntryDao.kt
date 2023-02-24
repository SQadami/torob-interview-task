package ir.torob.db.room.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ir.torob.data.model.EntryWithProduct
import ir.torob.data.model.PaginatedEntry
import ir.torob.db.dao.PaginatedEntryDao

interface RoomPaginatedEntryDao<
        EC : PaginatedEntry,
        LI : EntryWithProduct<EC>> : RoomEntryDao<EC, LI>, PaginatedEntryDao<EC, LI> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun upsert(entity: EC) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun upsertAll(vararg entity: EC)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun upsertAll(entities: List<EC>)
}