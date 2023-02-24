package ir.torob.sample.ui.product.detail

import ir.torob.data.model.Product
import ir.torob.data.model.emptyContent
import ir.torob.imageloader.binding.bind
import ir.torob.sample.R
import ir.torob.sample.databinding.RowItemProductDetailBinding
import ir.torob.ui.extension.visibleIf
import ir.torob.ui.widget.recyclerview.RecyclerViewHolder

class ProductDetailViewHolder(
    private val binding: RowItemProductDetailBinding, adapter: ProductDetailAdapter
) : RecyclerViewHolder<Product>(binding.root, adapter) {

    override fun bind(item: Product) {
        if (item.emptyContent()) {
            bindLoading(true)
        } else {
            bindLoading(false)
            bindDetail(item)
        }
    }

    private fun bindLoading(loading: Boolean) = with(binding) {
        arrayOf(
            image,
            name1,
            name2,
            btnShare,
            btnLike,
            btnNotif
        ).forEach {
            it.visibleIf(!loading, gone = false)
        }
        progressBar.visibleIf(loading)
    }

    fun updateProduct(product: Product) {
        bindLoading(false)
        bindDetail(product)
    }

    private fun bindDetail(item: Product) = with(binding) {
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