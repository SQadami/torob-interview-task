package ir.torob.ui.widget.pageindicator

import android.animation.ArgbEvaluator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import ir.torob.ui.R

class PageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseDotsIndicator(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_WIDTH_FACTOR = 2.5f
    }

    private var linearLayout: LinearLayout? = null
    private var dotsWidthFactor: Float = 0f
    private var progressMode: Boolean = false

    var selectedDotColor: Int = 0
        set(value) {
            field = value
            refreshDotsColors()
        }

    private val argbEvaluator = ArgbEvaluator()

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        linearLayout = LinearLayout(context)
        linearLayout!!.orientation = LinearLayout.HORIZONTAL
        addView(
            linearLayout,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dotsWidthFactor = DEFAULT_WIDTH_FACTOR

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator)

            selectedDotColor =
                a.getColor(R.styleable.PageIndicator_selectedDotColor, DEFAULT_POINT_COLOR)

            dotsWidthFactor = a.getFloat(R.styleable.PageIndicator_dotsWidthFactor, 2.5f)
            if (dotsWidthFactor < 1) {
                dotsWidthFactor = 2.5f
            }

            progressMode = a.getBoolean(R.styleable.PageIndicator_progressMode, false)

            a.recycle()
        }

        if (isInEditMode) {
            addDots(5)
            refreshDots()
        }
    }

    override fun addDot(index: Int) {
        val dot = LayoutInflater.from(context).inflate(R.layout.layout_dot, this, false) as View
        val imageView = dot.findViewById(R.id.dot) as ImageView
        val params = imageView.layoutParams as LayoutParams

        dot.layoutDirection = View.LAYOUT_DIRECTION_LTR

        params.height = dotsSize.toInt()
        params.width = params.height
        params.setMargins(dotsSpacing.toInt(), 0, dotsSpacing.toInt(), 0)
        val background = DotsGradientDrawable()
        background.cornerRadius = dotsCornerRadius
        if (isInEditMode) {
            background.setColor(if (0 == index) selectedDotColor else dotsColor)
        } else {
            background.setColor(if (pager!!.currentItem == index) selectedDotColor else dotsColor)
        }
        imageView.background = background

        dot.setOnClickListener {
            if (dotsClickable && index < pager?.count ?: 0) {
                pager!!.setCurrentItem(index, true)
            }
        }

        dots.add(imageView)
        linearLayout!!.addView(dot)
    }

    override fun removeDot(index: Int) {
        linearLayout!!.removeViewAt(childCount - 1)
        dots.removeAt(dots.size - 1)
    }

    override fun buildOnPageChangedListener(): OnPageChangeListenerHelper {
        return object : OnPageChangeListenerHelper() {
            override fun onPageScrolled(
                selectedPosition: Int,
                nextPosition: Int,
                positionOffset: Float
            ) {
                val selectedDot = dots[selectedPosition]
                // Selected dot
                val selectedDotWidth =
                    (dotsSize + dotsSize * (dotsWidthFactor - 1) * (1 - positionOffset)).toInt()
                selectedDot.setWidth(selectedDotWidth)

                if (dots.isInBounds(nextPosition)) {
                    val nextDot = dots[nextPosition]

                    val nextDotWidth =
                        (dotsSize + dotsSize * (dotsWidthFactor - 1) * positionOffset).toInt()
                    nextDot.setWidth(nextDotWidth)

                    val selectedDotBackground = selectedDot.background as DotsGradientDrawable
                    val nextDotBackground = nextDot.background as DotsGradientDrawable

                    if (selectedDotColor != dotsColor) {
                        val selectedColor = argbEvaluator.evaluate(
                            positionOffset, selectedDotColor,
                            dotsColor
                        ) as Int
                        val nextColor = argbEvaluator.evaluate(
                            positionOffset, dotsColor,
                            selectedDotColor
                        ) as Int

                        nextDotBackground.setColor(nextColor)

                        if (progressMode && selectedPosition <= pager!!.currentItem) {
                            selectedDotBackground.setColor(selectedDotColor)
                        } else {
                            selectedDotBackground.setColor(selectedColor)
                        }
                    }
                }

                invalidate()
            }

            override fun resetPosition(position: Int) {
                dots[position].setWidth(dotsSize.toInt())
                refreshDotColor(position)
            }

            override val pageCount: Int
                get() = dots.size
        }
    }

    override fun refreshDotColor(index: Int) {
        val elevationItem = dots[index]
        val background = elevationItem.background as DotsGradientDrawable

        if (index == pager!!.currentItem || progressMode && index < pager!!.currentItem) {
            background.setColor(selectedDotColor)
        } else {
            background.setColor(dotsColor)
        }

        elevationItem.background = background
        elevationItem.invalidate()
    }

    override val type get() = Type.DEFAULT

    //*********************************************************
    // Users Methods
    //*********************************************************

    @Deprecated("Use setSelectedDotColor() instead", ReplaceWith("setSelectedDotColor()"))
    fun setSelectedPointColor(color: Int) {
        selectedDotColor = color
    }
}