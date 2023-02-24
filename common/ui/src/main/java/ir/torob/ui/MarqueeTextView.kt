package ir.torob.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class MarqueeTextView : MaterialTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    )

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(true, direction, previouslyFocusedRect)
    }

    override fun onWindowFocusChanged(focused: Boolean) {
        super.onWindowFocusChanged(true)
    }

    override fun isFocused(): Boolean {
        return true
    }
}