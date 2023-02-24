package ir.torob.db.room.dao

import androidx.room.*
import ir.torob.data.model.LastRequest
import ir.torob.data.model.Request
import ir.torob.db.dao.LastRequestDao

@Dao
abstract class RoomLastRequestDao : LastRequestDao, RoomEntityDao<LastRequest> {

    @Query("SELECT * FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract override suspend fun lastRequest(request: Request, entityId: String): LastRequest?

    @Query("SELECT COUNT(*) FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract override suspend fun requestCount(request: Request, entityId: String): Int

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun upsert(entity: LastRequest) : Long
}