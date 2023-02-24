package ir.torob.sample.ui.product.detail

import ir.torob.data.model.Product
import ir.torob.imageloader.binding.bind
import ir.torob.sample.R
import ir.torob.sample.databinding.RowItemProductDetailBinding
import ir.torob.ui.widget.recyclerview.RecyclerViewHolder

class ProductDetailViewHolder(
    private val binding: RowItemProductDetailBinding, adapter: ProductDetailAdapter
) : RecyclerViewHolder<Product>(binding.root, adapter) {

    override fun bind(item: Product) = with(binding) {
        image.bind(
            imageUrl = item.imageUrl,
            cornerRadiusRes = R.dimen.corner_size,
            placeHolderDrawable = R.drawable.ic_image_placeholder,
            errorDrawable = R.drawable.ic_image_placeholder,
        )
        name1.text = item.name1
        name2.text = item.name1
    }
}