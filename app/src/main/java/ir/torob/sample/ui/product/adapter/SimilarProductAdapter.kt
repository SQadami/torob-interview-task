package ir.torob.sample.ui.product.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.torob.data.model.SimilarEntryWithProduct
import ir.torob.imageloader.binding.bind
import ir.torob.sample.R
import ir.torob.sample.databinding.RowItemProductBinding

class SimilarProductAdapter(
    var listener: OnItemClickListener
) :
    PagingDataAdapter<SimilarEntryWithProduct, SimilarProductAdapter.ProductItemViewHolder>(
        DiffUtilCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductItemViewHolder(
            this,
            RowItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        getItem(position)?.apply { holder.bind(this) }
    }

    class ProductItemViewHolder(
        private val adapter: SimilarProductAdapter,
        private val binding: RowItemProductBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                adapter.getItem(
                    bindingAdapterPosition
                )?.let { itemInPosition ->
                    Log.d("mammad", "item clicked:   ${itemInPosition.similar.name1}")
                    adapter.listener.onItemClicked(itemInPosition)
                }
            }
        }

        fun bind(item: SimilarEntryWithProduct) =
            with(binding) {
                image.bind(
                    imageUrl = item.similar.imageUrl,
                    cornerRadiusRes = R.dimen.corner_size,
                    placeHolderDrawable = R.drawable.ic_image_placeholder,
                    errorDrawable = R.drawable.ic_image_placeholder,
                )
                name.text = item.similar.name1
                price.text = item.similar.priceText
                shop.text = item.similar.shopText
            }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<SimilarEntryWithProduct>() {

        override fun areItemsTheSame(
            oldItem: SimilarEntryWithProduct,
            newItem: SimilarEntryWithProduct
        ): Boolean {
            return oldItem.product.randomKey == newItem.product.randomKey
        }

        override fun areContentsTheSame(
            oldItem: SimilarEntryWithProduct,
            newItem: SimilarEntryWithProduct
        ): Boolean {
            return oldItem.entry.productKey == newItem.entry.productKey &&
                    oldItem.entry.similarKey == newItem.entry.similarKey &&
                    oldItem.entry.page == newItem.entry.page
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(product: SimilarEntryWithProduct)
    }
}