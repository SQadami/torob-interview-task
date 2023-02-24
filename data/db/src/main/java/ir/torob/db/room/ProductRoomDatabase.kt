package ir.torob.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.torob.data.model.LastRequest
import ir.torob.data.model.Product
import ir.torob.data.model.SimilarProductEntry
import ir.torob.db.ProductDatabase
import ir.torob.db.room.converter.RoomTypeConverters
import ir.torob.db.room.dao.RoomLastRequestDao
import ir.torob.db.room.dao.RoomProductDao
import ir.torob.db.room.dao.RoomSimilarDao

@Database(
    entities = [
        LastRequest::class,
        Product::class,
        SimilarProductEntry::class,
    ],
    version = 1,
)
@TypeConverters(RoomTypeConverters::class)
abstract class ProductRoomDatabase : RoomDatabase(), ProductDatabase {

    abstract override fun productDao(): RoomProductDao

    abstract override fun similarDao(): RoomSimilarDao

    abstract override fun lastRequestDao(): RoomLastRequestDao
}