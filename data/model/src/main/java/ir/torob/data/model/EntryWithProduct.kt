package ir.torob.data.model

import java.util.*

interface EntryWithProduct<ET : Entry> {

    var entry: ET
    val product: Product
    var similar: Product

    fun generateStableId(): Long {
        return Objects.hash(product.randomKey, similar.randomKey).toLong()
    }
}