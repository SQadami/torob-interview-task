@file:Suppress("DEPRECATION")

package ir.torob.ui.widget.rounded

import android.annotation.TargetApi
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import ir.torob.ui.R

class RoundableLayoutHelper(view: ViewGroup) {

    var attachedView: ViewGroup = view
    var path: Path? = null

    /** corner radius */
    var cornerLeftTop: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }

    var cornerRightTop: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }

    var cornerLeftBottom: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }

    var cornerRightBottom: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }


    /** side option means top and bottom corner */

    /**
     * if left side value existed,
     * leftTop and leftBottom value is equal to leftSide value.
     * this is made in consideration of the custom attribute of motion layout.
     * because Constraint only has maximum two custom attribute. (2.0.0-beta2)
     */
    var cornerLeftSide: Float = 0F
        set(value) {
            field = value

            if (field != 0F) {
                cornerLeftTop = field
                cornerLeftBottom = field
            }

            attachedView.postInvalidate()
        }

    var cornerRightSide: Float = 0F
        set(value) {
            field = value

            if (field != 0F) {
                cornerRightTop = field
                cornerRightBottom = field
            }

            attachedView.postInvalidate()
        }


    var cornerAll: Float = 0F
        set(value) {
            field = value

            if (field != 0F) {
                cornerLeftSide = field
                cornerRightSide = field
            }

            attachedView.postInvalidate()
        }

    /** background color */
    var backgroundColor: Int? = null
        set(@ColorInt value) {
            field = value
            attachedView.postInvalidate()
        }

    /** stroke & dash options */
    var strokeLineWidth: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }

    var strokeLineColor = 0XFFFFFFFF.toInt()
        set(@ColorInt value) {
            field = value
            attachedView.postInvalidate()
        }

    var dashLineGap: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }

    var dashLineWidth: Float = 0F
        set(value) {
            field = value
            attachedView.postInvalidate()
        }

    fun render(attrs: AttributeSet?) {
        attrs?.let {
            /** set corner radii */
            with(attachedView.context.obtainStyledAttributes(it, R.styleable.RoundableLayout)) {
                val defaultTopRadius = resources.getDimension(R.dimen.corner_radius_modal).toInt()
                cornerLeftTop =
                    getDimensionPixelSize(
                        R.styleable.RoundableLayout_cornerLeftTop,
                        defaultTopRadius
                    )
                        .toFloat()
                cornerRightTop =
                    getDimensionPixelSize(
                        R.styleable.RoundableLayout_cornerRightTop,
                        defaultTopRadius
                    )
                        .toFloat()
                cornerLeftBottom =
                    getDimensionPixelSize(R.styleable.RoundableLayout_cornerLeftBottom, 0)
                        .toFloat()
                cornerRightBottom =
                    getDimensionPixelSize(R.styleable.RoundableLayout_cornerRightBottom, 0)
                        .toFloat()
                backgroundColor =
                    getColor(R.styleable.RoundableLayout_backgroundColor, Color.WHITE)
                strokeLineWidth =
                    getDimensionPixelSize(R.styleable.RoundableLayout_strokeLineWidth, 0)
                        .toFloat()
                strokeLineColor =
                    getColor(R.styleable.RoundableLayout_strokeLineColor, Color.BLACK)
                dashLineWidth =
                    getDimensionPixelSize(R.styleable.RoundableLayout_dashLineWidth, 0)
                        .toFloat()
                dashLineGap =
                    getDimensionPixelSize(R.styleable.RoundableLayout_dashLineGap, 0)
                        .toFloat()
                cornerLeftSide =
                    getDimensionPixelSize(R.styleable.RoundableLayout_cornerLeftSide, 0)
                        .toFloat()
                cornerRightSide =
                    getDimensionPixelSize(R.styleable.RoundableLayout_cornerRightSide, 0)
                        .toFloat()
                cornerAll =
                    getDimensionPixelSize(R.styleable.RoundableLayout_cornerAll, 0)
                        .toFloat()
                recycle()
            }
        }
    }

    fun dispatchDraw(canvas: Canvas) {
        /** for outline remake whenenver draw */
        path = null

        if (path == null) {
            path = Path()
        }

        floatArrayOf(
            cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop, cornerRightBottom,
            cornerRightBottom, cornerLeftBottom, cornerLeftBottom
        ).let {
            clipPathCanvas(canvas, it)
        }

        /** set drawable resource corner & background & stroke */
        with(GradientDrawable()) {
            cornerRadii = floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop,
                cornerRightBottom, cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )

            if (strokeLineWidth != 0F)
                setStroke(strokeLineWidth.toInt(), strokeLineColor, dashLineWidth, dashLineGap)

            setColor(backgroundColor ?: Color.WHITE)

            attachedView.background = this
        }

        attachedView.outlineProvider = getOutlineProvider()
        attachedView.clipChildren = false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun getOutlineProvider(): ViewOutlineProvider {
        return object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                path?.let {
                    outline.setConvexPath(it)
                } ?: throw Exception()
            }
        }
    }

    fun setBackgroundColor(color: Int) {
        backgroundColor = color
    }

    private fun clipPathCanvas(canvas: Canvas, floatArray: FloatArray) {
        path?.let {
            it.addRoundRect(
                RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat()),
                floatArray,
                Path.Direction.CW
            )
            canvas.clipPath(it)
        }
    }
}