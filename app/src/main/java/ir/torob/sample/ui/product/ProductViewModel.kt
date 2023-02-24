package ir.torob.sample.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.torob.core.extension.shareWhileObserved
import ir.torob.data.model.Product
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.domain.observers.ObservePagedSimilarProducts
import ir.torob.domain.observers.ObserveProductDetail
import ir.torob.sample.util.productKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    productDetailInteractor: ObserveProductDetail,
    similarProductPagingInteractor: ObservePagedSimilarProducts,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productKey = savedStateHandle.productKey()

    val productDetail: Flow<Product> =
        productDetailInteractor.flow.shareWhileObserved(viewModelScope)

    val similarProductPagination: Flow<PagingData<SimilarEntryWithProduct>> =
        similarProductPagingInteractor.flow.shareWhileObserved(viewModelScope)

    init {
        productDetailInteractor(ObserveProductDetail.Params(productKey))
        similarProductPagingInteractor(
            ObservePagedSimilarProducts.Params(
                PAGING_CONFIG,
                productKey
            )
        )
    }

    companion object {
        val PAGING_CONFIG =
            PagingConfig(
                pageSize = 30,
                initialLoadSize = 30,
            )
    }
}