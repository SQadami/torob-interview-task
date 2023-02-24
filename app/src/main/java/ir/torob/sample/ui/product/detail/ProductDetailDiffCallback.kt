package ir.torob.sample.ui.product.detail

import androidx.recyclerview.widget.DiffUtil
import ir.torob.data.model.Product

class ProductDetailDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product) =
        oldItem.randomKey == newItem.randomKey

    override fun areContentsTheSame(oldItem: Product, newItem: Product) =
        oldItem.id == newItem.id &&
                oldItem.name1 == newItem.name1 &&
                oldItem.name2 == newItem.name2 &&
                oldItem.imageUrl == newItem.imageUrl &&
                oldItem.priceText == newItem.priceText &&
                oldItem.shopText == newItem.shopText
}