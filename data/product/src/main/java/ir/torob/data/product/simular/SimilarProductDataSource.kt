package ir.torob.data.product.simular

import ir.torob.data.model.Product

interface SimilarProductDataSource {

    suspend fun getSimilarProducts(
        productKey: String,
        page: Int,
        pageSize: Int,
    ): List<Product>
}