package ir.torob.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView

/**
 * A extension of MaterialCardView that is always 2:3 aspect ratio.
 */
class TowThreeCardView(context: Context?, attrs: AttributeSet?) : MaterialCardView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fourThreeHeight =
            MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec) * 2 / 3,
                MeasureSpec.EXACTLY
            )
        super.onMeasure(widthMeasureSpec, fourThreeHeight)
    }
}