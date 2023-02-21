package ir.torob.ui.binding

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import ir.torob.ui.R
import ir.torob.ui.extension.colorFromAttr

@BindingAdapter(
    "showProgress",
    "iconSource",
    "textSource",
    requireAll = false
)
fun MaterialButton.showProgress(
    showProgress: Boolean? = false,
    iconSource: Drawable? = null,
    textSource: String? = null
) {
    isEnabled = showProgress != true
    text = if (showProgress == true) "" else textSource

    icon = if (showProgress == true) {
        CircularProgressDrawable(context!!).apply {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(context.colorFromAttr(R.attr.colorAccent))
            start()
        }
    } else iconSource
    if (icon != null) { // callback to redraw button icon
        icon.callback = object : Drawable.Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            }

            override fun invalidateDrawable(who: Drawable) {
                this@showProgress.invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            }
        }
    }
}