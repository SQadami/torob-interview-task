package ir.torob.data.model

import androidx.room.*

@Entity(
    tableName = "similar_products",
    indices = [
        Index(value = ["product_key"], unique = false),
        Index(value = ["similar_key"], unique = false),
    ],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = arrayOf("random_key"),
            childColumns = arrayOf("similar_key"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class SimilarProductEntry(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "product_key") override val productKey: String,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "similar_key") val similarKey: String,
) : PaginatedEntry