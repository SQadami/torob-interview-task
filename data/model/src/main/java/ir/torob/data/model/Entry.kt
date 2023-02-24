package ir.torob.data.model

interface Entry : ProductEntity {
    val productKey: String
}

interface PaginatedEntry : Entry {
    val page: Int
}