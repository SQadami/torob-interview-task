package ir.torob.ui.widget.recyclerview

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerViewAdapter<VIEW_HOLDER : RecyclerViewHolder<MODEL>, MODEL> :
    RecyclerView.Adapter<VIEW_HOLDER>(),
    AsyncListDiffer.ListListener<MODEL> {

    abstract val asyncDiffer: AsyncListDiffer<MODEL>

    var itemClickListener: (MODEL) -> Unit = { }
    var listChangeListener: (() -> Unit)? = null

    private var notifyChange = false

    override fun onBindViewHolder(viewHolder: VIEW_HOLDER, position: Int) {
        viewHolder.bind(asyncDiffer.currentList[position])
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    override fun onCurrentListChanged(
        previousList: MutableList<MODEL>,
        currentList: MutableList<MODEL>
    ) {
        if (notifyChange) {
            listChangeListener?.invoke()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView is EmptyableRecyclerView) {
            recyclerView.attachDataObserver()
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (recyclerView is EmptyableRecyclerView) {
            recyclerView.detachDataObserver()
        }
        super.onDetachedFromRecyclerView(recyclerView)
    }

    open fun setItems(newItems: List<MODEL>, notifyChange: Boolean = false) {
        this.notifyChange = notifyChange
        with(asyncDiffer) {
            removeListListener(this@RecyclerViewAdapter)
            addListListener(this@RecyclerViewAdapter)
            submitList(newItems)
        }
    }

    open fun onDestroyView() {
        asyncDiffer.removeListListener(this)
    }

    fun getItems(): List<MODEL> = asyncDiffer.currentList

    @Suppress("ReplaceCallWithBinaryOperator")
    fun indexOf(item: MODEL) = asyncDiffer.currentList.indexOfFirst { it!!.equals(item) }

    fun addItems(item: List<MODEL>) {
        setItems(
            getItems()
                .toMutableList().apply {
                    addAll(0, item)
                }
        )
    }

    fun addItem(item: MODEL) {
        setItems(
            getItems()
                .toMutableList().apply {
                    add(0, item)
                }
        )
    }

    fun removeItem(item: MODEL) {
        setItems(
            getItems()
                .toMutableList().apply {
                    val toRemoveIndex = indexOf(item)
                    removeAt(toRemoveIndex)
                }
        )
    }
}