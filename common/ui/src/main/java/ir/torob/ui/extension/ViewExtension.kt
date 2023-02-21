package ir.torob.ui.extension

import android.graphics.Rect
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.ViewStubProxy
import ir.torob.ui.R
import ir.torob.ui.SafeOnClickListener

fun View.visible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.gone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.visibleIf(condition: Boolean, gone: Boolean = true) =
    if (condition) {
        visible()
    } else {
        if (gone) gone() else invisible()
    }

fun View.onClick(safe: Boolean = true, action: (View) -> Unit) =
    setOnClickListener(SafeOnClickListener(safe, action))

fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun <T : ViewDataBinding> View.viewBinding() = DataBindingUtil.getBinding<T>(this)!!

/**
 * inflate ViewStub if it wasn't inflated before
 */
fun ViewStubProxy.inflateOnce() {
    if (!this.isInflated) {
        viewStub?.inflate()
    }
}

fun ViewStubProxy.goneIfInflated() {
    if (this.isInflated) {
        root?.gone()
    }
}

fun ViewStubProxy.inflateAndVisible() {
    inflateOnce()
    root?.visible()
}

fun View.expandTouch(amount: Int = 50) {
    val rect = Rect()
    this.getHitRect(rect)
    rect.top -= amount
    rect.right += amount
    rect.bottom += amount
    rect.left -= amount
    (this.parent as View).touchDelegate = TouchDelegate(rect, this)
}