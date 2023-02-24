package ir.torob.sample.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.torob.core.extension.shareWhileObserved
import ir.torob.core.livedata.Event
import ir.torob.data.model.Product
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.domain.observers.ObservePagedSimilarProducts
import ir.torob.domain.observers.ObserveProductDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEY_PRODUCT = "productKey"
private const val INITIAL_PRODUCT_KEY = "c248e83f-2af8-4957-8a91-5e72a0f3acef"

@HiltViewModel
class ProductViewModel @Inject constructor(
    productDetailInteractor: ObserveProductDetail,
    similarProductPagingInteractor: ObservePagedSimilarProducts,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productKey = savedStateHandle.get<String>(KEY_PRODUCT) ?: INITIAL_PRODUCT_KEY
    private val _onError: (String?) -> Unit = {
        viewModelScope.launch {
            _pendingActions.emit(
                Event(ErrorMessageAction(it))
            )
        }
    }

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _pendingActions: MutableSharedFlow<Event<ProductUiAction>?> = MutableSharedFlow()

    val isLoading = _isLoading.asStateFlow()
    val pendingActions = _pendingActions.shareWhileObserved(viewModelScope)

    val productDetail: Flow<Product> = productDetailInteractor.flow

    val similarProductPagination: Flow<PagingData<SimilarEntryWithProduct>> =
        similarProductPagingInteractor.flow.cachedIn(viewModelScope)

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