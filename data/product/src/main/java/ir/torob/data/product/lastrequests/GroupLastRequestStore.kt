package ir.torob.data.product.lastrequests

import ir.torob.data.model.LastRequest
import ir.torob.data.model.Request
import ir.torob.db.dao.LastRequestDao
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

open class GroupLastRequestStore(
    private val request: Request,
    private val dao: LastRequestDao,
) {

    suspend fun getRequestInstant(): Instant? {
        return dao.lastRequest(request, DEFAULT_ID)?.timestamp
    }

    suspend fun isRequestExpired(threshold: Duration): Boolean {
        return isRequestBefore(Clock.System.now() - threshold)
    }

    suspend fun isRequestBefore(instant: Instant): Boolean {
        return getRequestInstant()?.let { it < instant } ?: true
    }

    suspend fun updateLastRequest(timestamp: Instant = Clock.System.now()) {
        dao.upsert(
            LastRequest(
                request = request,
                entityId = DEFAULT_ID,
                _timestamp = timestamp.toEpochMilliseconds(),
            ),
        )
    }

    suspend fun invalidateLastRequest() = updateLastRequest(Instant.DISTANT_PAST)

    companion object {
        private const val DEFAULT_ID = "default"
    }
}