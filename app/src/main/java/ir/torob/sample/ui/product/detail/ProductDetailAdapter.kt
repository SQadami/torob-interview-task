package ir.torob.sample.ui.product.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.torob.data.model.Product
import ir.torob.sample.databinding.RowItemProductDetailBinding

class ProductDetailAdapter(
    initialProductKey: String
) : RecyclerView.Adapter<ProductDetailViewHolder>() {

    private val items: MutableList<Product> = mutableListOf(Product(randomKey = initialProductKey))

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductDetailViewHolder(
            RowItemProductDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        viewHolder: ProductDetailViewHolder,
        position: Int
    ) = viewHolder.bind(items[position])

    override fun onBindViewHolder(
        viewHolder: ProductDetailViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) =
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads)
        } else {
            val product = payloads[0] as Product
            viewHolder.updateProduct(product)
        }

    fun setProduct(product: Product) {
        items[0] = product
        notifyItemChanged(0, product)
    }
}