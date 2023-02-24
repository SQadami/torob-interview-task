package ir.torob.domain

import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import ir.torob.data.model.EntryWithProduct
import ir.torob.data.model.PaginatedEntry

/**
 * A [RemoteMediator] which works on [PaginatedEntry] entities, but only calls
 * [fetch] for [LoadType.REFRESH] events.
 */
internal class RefreshOnlyRemoteMediator<LI, ET>(
    private val fetch: suspend () -> Unit,
) : RemoteMediator<Int, LI>() where ET : PaginatedEntry, LI : EntryWithProduct<ET> {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LI>,
    ): MediatorResult {
        if (loadType == LoadType.PREPEND || loadType == LoadType.APPEND) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }
        return try {
            fetch()
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}