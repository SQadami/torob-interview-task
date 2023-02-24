package ir.torob.data.model

import androidx.room.*

interface ProductEntity {
    val id: Long
}

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["random_key"], unique = true),
    ],
)
data class Product(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "random_key") val randomKey: String? = null,
    @ColumnInfo(name = "name1") val name1: String? = null,
    @ColumnInfo(name = "name2") val name2: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    @ColumnInfo(name = "price_text") val priceText: String? = null,
    @ColumnInfo(name = "shop_text") val shopText: String? = null,
) : ProductEntity {

    @Ignore
    constructor() : this(0)
}