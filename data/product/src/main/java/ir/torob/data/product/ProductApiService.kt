package ir.torob.data.product

import ir.torob.data.product.dto.ProductDto
import ir.torob.data.product.dto.SimilarProductListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {

    @GET("/v4/base-product/details/")
    fun loadDetail(
        @Query("prk") productKey: String,
    ): Call<ProductDto>

    @GET("/v4/base-product/similar-base-product/")
    fun loadSimilar(
        @Query("prk") productKey: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<SimilarProductListDto>
}