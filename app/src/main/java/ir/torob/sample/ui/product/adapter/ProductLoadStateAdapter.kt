package ir.torob.sample.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.torob.sample.databinding.RowLoadStateLayoutBinding
import ir.torob.ui.extension.onClick
import ir.torob.ui.extension.visibleIf

class ProductLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ProductLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(
            RowLoadStateLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), retry
        )

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: RowLoadStateLayoutBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.onClick { retry.invoke() }
        }

        fun bind(item: LoadState) {
            with(binding) {
                progressBar.visibleIf(item is LoadState.Loading)
                btnRetry.visibleIf(item !is LoadState.Loading)
            }
        }
    }
}