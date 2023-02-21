package ir.torob.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ir.torob.core.livedata.ConsumableLiveData

/**
 * This functions helps in transforming a [MutableLiveData] of type [T]
 * to a [LiveData] of type [T]
 */
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

/**
 * This function helps to observe a [LiveData] once
 */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    val consumableLiveData = ConsumableLiveData<T>()
    consumableLiveData.observe(lifecycleOwner, observer)
    observe(
        lifecycleOwner,
        { t ->
            consumableLiveData.setValue(t)
        }
    )
}
