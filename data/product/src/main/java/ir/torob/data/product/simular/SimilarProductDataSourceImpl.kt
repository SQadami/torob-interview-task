package ir.torob.data.product.simular

import ir.torob.data.model.Product
import ir.torob.data.product.ProductApiService
import ir.torob.data.product.dto.toModel
import ir.torob.network.bodyOrThrow
import ir.torob.network.withRetry
import retrofit2.awaitResponse

class SimilarProductDataSourceImpl(
    private val apiService: ProductApiService
) : SimilarProductDataSource {

    override suspend fun getSimilarProducts(
        productKey: String, page: Int, pageSize: Int
    ): List<Product> {
        return withRetry {
            apiService.loadSimilar(
                productKey = productKey,
                page = page,
                size = pageSize,
            ).awaitResponse()
                .bodyOrThrow()
                .results
                .map { productDto ->
                    productDto.toModel()
                }

        }
    }
}