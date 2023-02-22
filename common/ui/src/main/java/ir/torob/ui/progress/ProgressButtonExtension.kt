package ir.torob.ui.progress

import com.google.android.material.button.MaterialButton
import ir.torob.ui.R
import ir.torob.ui.extension.colorFromAttr
import ir.torob.ui.extension.stringRes

fun MaterialButton.startLoading() {
    isEnabled = false
    showProgress {
        progressColor = context.colorFromAttr(com.google.android.material.R.attr.colorAccent)
        gravity = DrawableButton.GRAVITY_CENTER
    }
}

fun MaterialButton.stopLoading(buttonText: String? = null) {
    isEnabled = true
    hideProgress(buttonText ?: text.toString())
}

fun MaterialButton.stopLoading(buttonTextRes: Int = 0) {
    stopLoading(
        if (buttonTextRes > 0) buttonTextRes.stringRes(context)
        else null
    )
}