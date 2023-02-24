package ir.torob.db

import ir.torob.db.dao.LastRequestDao
import ir.torob.db.dao.ProductDao
import ir.torob.db.dao.SimilarDao

interface ProductDatabase {

    fun productDao(): ProductDao

    fun similarDao(): SimilarDao

    fun lastRequestDao(): LastRequestDao
}