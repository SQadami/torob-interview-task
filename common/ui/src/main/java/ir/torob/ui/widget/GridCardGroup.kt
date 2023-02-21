package ir.torob.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData

/**
 * A GridLayout subclass that acts like a CheckBox/Radio Group.
 * Important: it only accepts CardView as children.
 */
class GridCardGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    GridLayout(context, attrs, defStyleAttr), View.OnClickListener {

    val selectedItem = MutableLiveData<Int?>(null)

    var checkedChangeListener: OnCheckedChangeListener? = null
    var singleSelectMode = true

    override fun onClick(view: View) {
        if (singleSelectMode)
            onClickSingleSelect(view)
        else
            onClickMultiSelect(view as CardView)
        if (singleSelectMode) {
            for (index in 0 until childCount) {
                val cardView = getChildAt(index) as CardView
                cardView.isSelected = false
            }

            val cardView = view as CardView
            cardView.isSelected = true
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        child?.setOnClickListener(this)
    }

    private fun getSelectedCard(): CardView? {
        for (index in 0 until childCount) {
            val cardView = getChildAt(index) as CardView
            if (cardView.isSelected) return cardView
        }
        return null
    }

    private fun onClickMultiSelect(view: CardView) {
        val isChecked = view.isSelected.not()
        view.dispatchSetSelected(isChecked)
        checkedChangeListener?.onCheckedChanged(view, isChecked)
    }

    private fun onClickSingleSelect(view: View) {
        val prevSelectedItemId = getSelectedCard()?.id
        if (prevSelectedItemId == null ||
            view.id != prevSelectedItemId
        ) {
            for (index in 0 until childCount) {
                val cardView = getChildAt(index) as CardView
                cardView.isSelected = false
            }

            val cardView = view as CardView
            cardView.isSelected = true

            selectedItem.value = view.id
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: CardView, isChecked: Boolean)
    }
}