package ir.torob.ui.widget.recyclerview.layout_manager

import android.content.Context
import android.util.AttributeSet
import android.util.LayoutDirection
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridManager : GridLayoutManager {

    constructor(context: Context?, spanCount: Int) : super(context, spanCount)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context?,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ignored: Exception) {
        }
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        try {
            super.onMeasure(recycler, state, widthSpec, heightSpec)
        } catch (ignored: Exception) {
        }
    }

    override fun getLayoutDirection() = LayoutDirection.RTL
}