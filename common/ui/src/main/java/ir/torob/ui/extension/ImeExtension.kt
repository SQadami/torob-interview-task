package ir.torob.ui.extension

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

fun hideKeyboard(view: View?) {
    view ?: return
    (view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboard() {
    try {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(
                (currentFocus ?: View(this)).windowToken,
                0
            )
    } catch (e: Exception) {
    }
}

fun Fragment.hideKeyboard() = activity?.hideKeyboard()

fun EditText.showKeyboard() {
    try {
        requestFocus()
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, 0)
    } catch (e: Exception) {
    }
}

fun EditText.hideKeyboard() {
    hideKeyboard(this)
}