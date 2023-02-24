package ir.torob.db.dao

import ir.torob.data.model.ProductEntity

interface EntityDao<in E : ProductEntity> {

    suspend fun upsert(entity: E): Long
    suspend fun upsertAll(vararg entity: E)
    suspend fun upsertAll(entities: List<E>)
    suspend fun update(entity: E)
    suspend fun deleteEntity(entity: E): Int
}