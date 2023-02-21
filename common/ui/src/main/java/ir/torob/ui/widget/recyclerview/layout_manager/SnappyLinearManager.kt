package ir.torob.ui.widget.recyclerview.layout_manager

import android.content.Context
import android.graphics.PointF
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.ViewConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ln


class SnappyLinearManager : LinearLayoutManager {

    private var deceleration = 0.0

    constructor(context: Context) : super(context) {
        calculateDeceleration(context)
    }

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
        calculateDeceleration(context)
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        calculateDeceleration(context)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                // I want a behavior where the scrolling always snaps to the beginning of
                // the list. Snapping to end is also trivial given the default implementation.
                // If you need a different behavior, you may need to override more
                // of the LinearSmoothScrolling methods.
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                    return this@SnappyLinearManager
                        .computeScrollVectorForPosition(targetPosition)
                }
            }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    /**
     * @param velocityX
     * @param velocityY
     * @return the resultant position from a fling of the given velocity.
     */
    fun getPositionForVelocity(velocityX: Int, velocityY: Int): Int {
        if (childCount == 0) {
            return 0
        }
        return if (orientation == HORIZONTAL) {
            calcPosForVelocity(
                velocityX,
                getChildAt(0)!!.left,
                getChildAt(0)!!.width,
                getPosition((getChildAt(0))!!)
            )
        } else {
            calcPosForVelocity(
                velocityY,
                getChildAt(0)!!.top,
                getChildAt(0)!!.height,
                getPosition((getChildAt(0))!!)
            )
        }
    }

    /**
     * This implementation obviously doesn't take into account the direction of the
     * that preceded it, but there is no easy way to get that information without more
     * hacking than I was willing to put into it.
     *
     * @return the position this list must scroll to to fix a state where the
     * views are not snapped to grid.
     */
    fun getFixScrollPos(): Int {
        if (this.childCount == 0) {
            return 0
        }
        val child = getChildAt(0)
        val childPos = getPosition((child)!!)
        if (
            orientation == HORIZONTAL &&
            kotlin.math.abs(child.left) > child.measuredWidth / 2
        ) {
            // Scrolled first view more than halfway offscreen
            return childPos + 1
        } else if (
            orientation == VERTICAL &&
            kotlin.math.abs(child.top) > child.measuredWidth / 2
        ) {
            // Scrolled first view more than halfway offscreen
            return childPos + 1
        }
        return childPos
    }

    private fun calculateDeceleration(context: Context) {
        deceleration = (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.3700787 // inches per meter
                // pixels per inch. 160 is the "default" dpi, i.e. one dip is one pixel on a 160 dpi
                // screen
                * context.resources.displayMetrics.density * 160.0f * FRICTION)
    }

    private fun calcPosForVelocity(
        velocity: Int,
        scrollPos: Int,
        childSize: Int,
        currPos: Int
    ): Int {
        val dist = getSplineFlingDistance(velocity.toDouble())
        val tempScroll = scrollPos + if (velocity > 0) dist else -dist
        return if (velocity < 0) {
            // Not sure if I need to lower bound this here.
            (currPos + tempScroll / childSize).coerceAtLeast(0.0).toInt()
        } else {
            (currPos + (tempScroll / childSize) + 1).toInt()
        }
    }

    private fun getSplineFlingDistance(velocity: Double): Double {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = DECELERATION_RATE - 1.0
        return (ViewConfiguration.getScrollFriction() *
                deceleration *
                kotlin.math.exp(DECELERATION_RATE / decelMinusOne * l))
    }

    private fun getSplineDeceleration(velocity: Double): Double {
        return ln(
            INFLEXION *
                    kotlin.math.abs(velocity) /
                    (ViewConfiguration.getScrollFriction() * deceleration)
        )
    }

    companion object {
        // These variables are from android.widget.Scroller, which is used, via ScrollerCompat, by
        // Recycler View. The scrolling distance calculation logic originates from the same place. Want
        // to use their variables so as to approximate the look of normal Android scrolling.
        // Find the Scroller fling implementation in android.widget.Scroller.fling().
        private const val INFLEXION = 0.35f // Tension lines cross at (INFLEXION, 1)
        private const val FRICTION = 0.84

        private val DECELERATION_RATE = (ln(0.78) / ln(0.9)).toFloat()
    }
}