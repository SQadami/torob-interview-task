package ir.torob.sample.ui.product.similar

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimilarProductDecoration(
    private val spanCount: Int = 2,
    private val spacing: Int = 0,
    private val includeEdge: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val globalPosition = parent.getChildAdapterPosition(view)
        val viewHolder = parent.getChildViewHolder(view)
        if (viewHolder !is SimilarProductAdapter.ProductItemViewHolder) return

        val position = globalPosition - 1
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = (column + 1) * spacing / spanCount
            outRect.right = spacing - column * spacing / spanCount
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = spacing - (column + 1) * spacing / spanCount
            outRect.right = column * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}