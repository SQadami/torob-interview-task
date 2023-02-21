package ir.torob.core.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

fun <T> Flow<T>.shareWhileObserved(coroutineScope: CoroutineScope) = shareIn(
    scope = coroutineScope,
    started = SharingStarted.WhileSubscribed(),
    replay = 1
)

fun <T> createMutableSharedFlow(): MutableSharedFlow<T> =
    MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

inline fun <reified T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
): Job =
    lifecycleOwner.lifecycleScope.launch {
        flowWithLifecycle(
            lifecycleOwner.lifecycle,
            minActiveState
        ).collect(action)
    }

inline fun <reified T> Flow<T>.observeWithLifecycle(
    fragment: Fragment,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
): Job =
    fragment.viewLifecycleOwner.lifecycleScope.launch {
        flowWithLifecycle(
            fragment.viewLifecycleOwner.lifecycle,
            minActiveState
        ).collect(action)
    }

inline fun <reified T> Flow<T>.observeWithLifecycle(
    activity: FragmentActivity,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
): Job =
    activity.lifecycleScope.launch {
        flowWithLifecycle(
            activity.lifecycle,
            minActiveState
        ).collect(action)
    }
