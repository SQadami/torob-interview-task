package ir.torob.ui.widget.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class SquareImageView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size: Int = if (
            (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)
                .xor(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY)
        ) {
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) width
            else height
        } else width.coerceAtMost(height)

        setMeasuredDimension(size, size)
    }
}