package ir.torob.domain.observers

import ir.torob.core.AppCoroutineDispatchers
import ir.torob.data.model.Product
import ir.torob.data.product.detail.ProductDetailStore
import ir.torob.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreRequest
import org.mobilenativefoundation.store.store5.StoreResponse
import javax.inject.Inject

class ObserveProductDetail @Inject constructor(
    private val productDetailStore: ProductDetailStore,
    private val dispatchers: AppCoroutineDispatchers,
) : SubjectInteractor<ObserveProductDetail.Params, Product>() {

    override fun createObservable(params: Params): Flow<Product> {
        return productDetailStore.stream(StoreRequest.cached(params.productKey, refresh = true))
            .filter { it is StoreResponse.Data }
            .map { it.requireData() }
            .flowOn(dispatchers.computation)
    }

    data class Params(val productKey: String)
}