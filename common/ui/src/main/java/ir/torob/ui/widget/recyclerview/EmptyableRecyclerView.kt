package ir.torob.ui.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ir.torob.ui.extension.gone
import ir.torob.ui.extension.visibleIf

/**
 * [RecyclerView] that shows [emptyView] if the adapter is empty.
 */
class EmptyableRecyclerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    /**
     * Sets the [View] that should be used when the adapter is empty.
     */
    var emptyView: View? = null
        set(newView) {
            field = newView
            checkIfEmpty()
        }

    /**
     * Sets the [View] that should be used when the content is loading.
     */
    var loadingView: View? = null
    var isLoading: Boolean = false
        set(newValue) {
            field = newValue
            checkIfLoading()
        }

    private val adapterObserver = object : AdapterDataObserver() {

        override fun onChanged() = checkIfEmpty()

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkIfEmpty()

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkIfEmpty()
    }

    companion object {
        const val TAG = "EmptyableRecyclerView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroyView()
    }

    fun attachDataObserver() = try {
        adapter!!.registerAdapterDataObserver(adapterObserver)
        checkIfEmpty()
        Log.d(TAG, "empty checker dataObserver attached to recyclerView adapter")
    } catch (e: Exception) {
        Log.e(TAG, "fail to attach:   ", e)
    }

    fun detachDataObserver() = try {
        adapter!!.unregisterAdapterDataObserver(adapterObserver)
        Log.d(TAG, "empty checker dataObserver detached from adapter")
    } catch (e: Exception) {
        Log.e(TAG, "fail to detach:   ", e)
    }

    private fun onDestroyView() {
        adapter?.run {
            if (this is RecyclerViewAdapter<*, *>)
                onDestroyView()
            adapter = null
        }
    }

    private fun checkIfEmpty() {
        if (isLoading) return

        adapter?.let { a ->
            emptyView?.let { eV ->
                val emptyViewVisible = a.itemCount == 0

                eV.visibleIf(emptyViewVisible)
                visibleIf(!emptyViewVisible)
            }
        }
    }

    private fun checkIfLoading() {
        loadingView?.let { lV ->
            lV.visibleIf(isLoading)

            if (isLoading) {
                emptyView?.gone()
                gone()
            } else {
                checkIfEmpty()
            }
        }
    }
}