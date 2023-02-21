package ir.torob.ui.widget.rounded

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout

class RoundableCoordinatorLayout : CoordinatorLayout {

    var roundableLayoutHelper: RoundableLayoutHelper

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        roundableLayoutHelper = RoundableLayoutHelper(this)
        roundableLayoutHelper.render(attrs)
    }

    constructor(context: Context) : super(context) {
        roundableLayoutHelper = RoundableLayoutHelper(this)
        roundableLayoutHelper.render(null)
    }

    override fun dispatchDraw(canvas: Canvas) {
        roundableLayoutHelper.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

    override fun setBackgroundColor(color: Int) {
        roundableLayoutHelper.backgroundColor = color
    }
}