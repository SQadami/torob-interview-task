package ir.torob.domain

import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import ir.torob.data.model.EntryWithProduct
import ir.torob.data.model.PaginatedEntry

/**
 * A [RemoteMediator] which works on [PaginatedEntry] entities. [fetch] will be called with the
 * next page to load.
 */

internal class PaginatedEntryRemoteMediator<LI, ET>(
    private val fetch: suspend (page: Int) -> Unit,
) : RemoteMediator<Int, LI>() where ET : PaginatedEntry, LI : EntryWithProduct<ET> {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LI>,
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.entry.page + 1
            }
        }
        return try {
            fetch(nextPage)
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}