package ir.torob.data.product.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarProductListDto(
    @Json(name = "results") val results: List<ProductDto>,
    @Json(name = "count") val count: Int
)