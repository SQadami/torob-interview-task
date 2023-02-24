package ir.torob.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.util.*

class SimilarEntryWithProduct : EntryWithProduct<SimilarProductEntry> {

    @Embedded
    override lateinit var entry: SimilarProductEntry

    @Relation(parentColumn = "product_key", entityColumn = "random_key")
    override lateinit var product: Product

    @Relation(parentColumn = "similar_key", entityColumn = "random_key")
    override lateinit var similar: Product

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SimilarEntryWithProduct -> {
            entry == other.entry &&
                    similar.randomKey == other.similar.randomKey &&
                    product.randomKey == other.product.randomKey
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, product, similar)
}