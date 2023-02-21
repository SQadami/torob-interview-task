package ir.torob.ui.widget

import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class ColoredClickableSpan(
    private val spanColor: Int
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.linkColor = spanColor
        ds.color = ds.linkColor
    }
}