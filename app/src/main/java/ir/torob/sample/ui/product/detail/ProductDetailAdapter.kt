package ir.torob.sample.ui.product.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import ir.torob.data.model.Product
import ir.torob.sample.databinding.RowItemProductDetailBinding
import ir.torob.ui.widget.recyclerview.RecyclerViewAdapter

class ProductDetailAdapter : RecyclerViewAdapter<ProductDetailViewHolder, Product>() {

    override val asyncDiffer = AsyncListDiffer(this, ProductDetailDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductDetailViewHolder(
            RowItemProductDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), this
        )

    override fun onBindViewHolder(
        viewHolder: ProductDetailViewHolder,
        position: Int
    ) {
        val item = asyncDiffer.currentList[position]
        viewHolder.bind(item)
    }
}