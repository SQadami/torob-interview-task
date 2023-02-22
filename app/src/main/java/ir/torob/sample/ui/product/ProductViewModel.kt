package ir.torob.sample.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.torob.core.extension.shareWhileObserved
import ir.torob.core.livedata.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {

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


}