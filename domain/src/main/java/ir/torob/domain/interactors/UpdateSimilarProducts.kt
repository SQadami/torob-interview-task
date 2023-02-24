package ir.torob.domain.interactors

import ir.torob.core.AppCoroutineDispatchers
import ir.torob.data.product.simular.SimilarProductStore
import ir.torob.data.product.simular.SimilarProductStoreKey
import ir.torob.data.product.util.fetch
import ir.torob.db.dao.SimilarDao
import ir.torob.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateSimilarProducts @Inject constructor(
    private val similarDao: SimilarDao,
    private val similarProductStore: SimilarProductStore,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateSimilarProducts.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val page = when {
                params.page >= 0 -> params.page
                params.page == Page.NEXT_PAGE -> {
                    val lastPage = similarDao.getLastPage()
                    if (lastPage != null) lastPage + 1 else 0
                }

                else -> 0
            }

            similarProductStore.fetch(
                SimilarProductStoreKey(
                    productKey = params.productKey,
                    page = page,
                    pageSize = params.pageSize,
                ),
                params.forceRefresh
            )
        }
    }

    data class Params(
        val productKey: String,
        val page: Int,
        val pageSize: Int,
        val forceRefresh: Boolean = false,
    )

    object Page {
        const val NEXT_PAGE = -1
        const val REFRESH = -2
    }
}