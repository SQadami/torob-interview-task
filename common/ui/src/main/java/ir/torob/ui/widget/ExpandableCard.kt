package ir.torob.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import ir.torob.ui.R
import ir.torob.ui.extension.onClick
import ir.torob.ui.extension.visibleIf

class ExpandableCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    MaterialCardView(context, attrs, defStyleAttr) {

    private var isExpanded: Boolean = false
    var toExpandView: View? = null

    val textView by lazy { findViewById<TextView>(R.id.text) }
    val iconView by lazy { findViewById<ImageView>(R.id.icon) }

    init {
        inflate(context, R.layout.view_expandable_card, this)

        attrs?.let { initAttr(it) }
        setExpanded(false)

        onClick { setExpanded(isExpanded.not()) }
    }

    fun collapse() = setExpanded(false)

    fun expand() = setExpanded(false)

    private fun initAttr(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCard)
        try {
            val text = a.getString(R.styleable.ExpandableCard_android_text)
            textView.text = text
        } finally {
            a.recycle()
        }
    }

    private fun setExpanded(isExpanded: Boolean) {
        this.isExpanded = isExpanded

        iconView.setImageResource(
            if (isExpanded) R.drawable.ic_arrow_up
            else R.drawable.ic_chevron_right
        )
        iconView.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                context,
                if (isExpanded) R.color.colorPrimary
                else R.color.blue_400
            )
        )
        toExpandView?.run { visibleIf(isExpanded) }
    }
}