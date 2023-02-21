package ir.torob.ui.widget.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewHolder<MODEL>(
    itemView: View,
    private val adapter: RecyclerViewAdapter<*, MODEL>
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: MODEL)

    protected fun getItemModel() = adapter.getItems()[adapterPosition]
}