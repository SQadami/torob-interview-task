package ir.torob.data.product.detail

import ir.torob.data.model.Product
import ir.torob.data.product.ProductApiService
import ir.torob.network.bodyOrThrow
import ir.torob.network.withRetry
import retrofit2.awaitResponse

class ProductDetailDataSourceImpl(
    private val apiService: ProductApiService
) : ProductDetailDataSource {

    override suspend fun getProductDetail(productKey: String): Product {
        return withRetry {
            apiService.loadDetail(productKey)
                .awaitResponse()
                .bodyOrThrow()
        }
    }
}