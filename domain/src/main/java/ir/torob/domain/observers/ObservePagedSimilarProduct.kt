package ir.torob.domain.observers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.db.dao.SimilarDao
import ir.torob.domain.PaginatedEntryRemoteMediator
import ir.torob.domain.PagingInteractor
import ir.torob.domain.interactors.UpdateSimilarProducts
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePagedSimilarProducts @Inject constructor(
    private val similarDao: SimilarDao,
    private val updateSimilarProducts: UpdateSimilarProducts,
) : PagingInteractor<ObservePagedSimilarProducts.Params, SimilarEntryWithProduct>() {

    override fun createObservable(
        params: Params,
    ): Flow<PagingData<SimilarEntryWithProduct>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedEntryRemoteMediator { page ->
                updateSimilarProducts.executeSync(
                    UpdateSimilarProducts.Params(
                        productKey = params.productKey,
                        page = page,
                        pageSize = params.pagingConfig.pageSize,
                        forceRefresh = true
                    ),
                )
            },
            pagingSourceFactory = similarDao::entriesPagingSource,
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val productKey: String,
    ) : Parameters<SimilarEntryWithProduct>
}