package ir.torob.ui.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import ir.torob.ui.widget.recyclerview.layout_manager.SnappyLinearManager

class SnappyRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) :
            super(context, attrs, defStyle)

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val lm = layoutManager
        if (lm is SnappyLinearManager) {
            val slm = layoutManager as SnappyLinearManager
            super.smoothScrollToPosition(slm.getPositionForVelocity(velocityX, velocityY))
            return true
        }
        return super.fling(velocityX, velocityY)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // We want the parent to handle all touch events--there's a lot going on there,
        // and there is no reason to overwrite that functionality--bad things will happen.
        val ret = super.onTouchEvent(e)
        val lm = layoutManager
        if (lm is SnappyLinearManager
            && (e.action == MotionEvent.ACTION_UP ||
                    e.action == MotionEvent.ACTION_CANCEL)
            && scrollState == SCROLL_STATE_IDLE
        ) {
            // The layout manager is a SnappyLayoutManager, which means that the
            // children should be snapped to a grid at the end of a drag or
            // fling. The motion event is either a user lifting their finger or
            // the cancellation of a motion events, so this is the time to take
            // over the scrolling to perform our own functionality.
            // Finally, the scroll state is idle--meaning that the resultant
            // velocity after the user's gesture was below the threshold, and
            // no fling was performed, so the view may be in an unaligned state
            // and will not be flung to a proper state.
            smoothScrollToPosition(lm.getFixScrollPos())
        }
        return ret
    }
}