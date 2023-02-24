package ir.torob.data.product.lastrequests

import ir.torob.data.model.LastRequest
import ir.torob.data.model.Request
import ir.torob.db.dao.LastRequestDao
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

abstract class EntityLastRequestStore(
    private val request: Request,
    private val dao: LastRequestDao,
) {

    private suspend fun getRequestInstant(entityId: String): Instant? {
        return dao.lastRequest(request, entityId)?.timestamp
    }

    suspend fun isRequestExpired(entityId: String, threshold: Duration): Boolean {
        return isRequestBefore(entityId, Clock.System.now() - threshold)
    }

    suspend fun hasBeenRequested(entityId: String): Boolean = dao.requestCount(request, entityId) > 0

    suspend fun isRequestBefore(entityId: String, instant: Instant): Boolean {
        return getRequestInstant(entityId)?.let { it < instant } ?: true
    }

    suspend fun updateLastRequest(entityId: String, timestamp: Instant = Clock.System.now()) {
        dao.upsert(
            LastRequest(
                request = request,
                entityId = entityId,
                _timestamp = timestamp.toEpochMilliseconds(),
            ),
        )
    }

    private suspend fun invalidateLastRequest(entityId: String) =
        updateLastRequest(entityId, Instant.DISTANT_PAST)
}