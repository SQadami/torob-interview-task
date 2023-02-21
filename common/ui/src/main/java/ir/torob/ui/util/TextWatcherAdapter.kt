package ir.torob.ui.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect

interface TextWatcherAdapter : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(s: Editable?) = Unit
}

fun EditText.onQueryTextChanged(): ReceiveChannel<String> =
    Channel<String>(capacity = Channel.UNLIMITED).also { channel ->
        addTextChangedListener(object : TextWatcherAdapter {
            override fun afterTextChanged(s: Editable?) {
                (s ?: "").let { channel.trySend(it.toString()).isSuccess }
            }
        })
    }

fun <T> ReceiveChannel<T>.debounce(
    time: Long,
    scope: CoroutineScope
): ReceiveChannel<T> =
    Channel<T>(capacity = Channel.CONFLATED).also { channel ->
        scope.launch {
            var value = receive()
            whileSelect {
                onTimeout(time) {
                    channel.trySend(value).isSuccess
                    value = receive()
                    true
                }
                onReceive {
                    value = it
                    true
                }
            }
        }
    }