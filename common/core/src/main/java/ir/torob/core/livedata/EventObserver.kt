package ir.torob.core.livedata

import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventUnconsumedContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.getContentIfNotHandled()?.run(onEventUnconsumedContent)
    }
}