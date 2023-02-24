package ir.torob.data.product.detail

import ir.torob.data.model.Product

interface ProductDetailDataSource {

    suspend fun getProductDetail(productKey: String): Product
}