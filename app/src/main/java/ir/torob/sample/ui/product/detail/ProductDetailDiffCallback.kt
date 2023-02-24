package ir.torob.sample.ui.product.detail

import androidx.recyclerview.widget.DiffUtil
import ir.torob.data.model.Product
import ir.torob.data.model.sameContent

class ProductDetailDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product) =
        oldItem.randomKey == newItem.randomKey

    override fun areContentsTheSame(oldItem: Product, newItem: Product) =
        oldItem.sameContent(newItem)
}