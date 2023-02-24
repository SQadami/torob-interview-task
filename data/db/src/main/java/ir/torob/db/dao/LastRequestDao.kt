package ir.torob.db.dao

import ir.torob.data.model.LastRequest
import ir.torob.data.model.Request

interface LastRequestDao : EntityDao<LastRequest> {

    suspend fun lastRequest(request: Request, entityId: String): LastRequest?

    suspend fun requestCount(request: Request, entityId: String): Int

    override suspend fun upsert(entity: LastRequest) : Long
}