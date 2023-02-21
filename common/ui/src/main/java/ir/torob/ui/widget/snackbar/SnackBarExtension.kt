package ir.torob.ui.widget.snackbar

import android.app.Activity
import androidx.fragment.app.Fragment
import ir.torob.ui.extension.hideKeyboard
import ir.torob.ui.extension.stringRes

fun Fragment.showSnackBar(message: String?) {
    message ?: return
    hideKeyboard()
    view?.let { MaterialSnackBar.make(it, message).show() }
}

fun Activity.showSnackBar(message: String?) {
    message ?: return
    hideKeyboard()
    MaterialSnackBar.make(findViewById(android.R.id.content), message).show()
}

fun Fragment.showSnackBar(messageRes: Int) =
    context?.let { showSnackBar(messageRes.stringRes(it)) }

fun Activity.showSnackBar(messageRes: Int) =
    showSnackBar(messageRes.stringRes(this))