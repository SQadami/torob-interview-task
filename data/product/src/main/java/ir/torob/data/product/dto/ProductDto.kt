package ir.torob.data.product.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(
    @Json(name = "random_key") val randomKey: String? = null,
    @Json(name = "name1") val name1: String? = null,
    @Json(name = "name2") val name2: String? = null,
    @Json(name = "image_url") val imageUrl: String? = null,
    @Json(name = "price_text") val priceText: String? = null,
    @Json(name = "shop_text") val shopText: String? = null,
)