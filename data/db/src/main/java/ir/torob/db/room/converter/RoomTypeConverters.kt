package ir.torob.db.room.converter

import androidx.room.TypeConverter
import ir.torob.data.model.Request
import ir.torob.db.extension.unsafeLazy

object RoomTypeConverters {

    private val requestValues by unsafeLazy { Request.values() }

    @TypeConverter
    @JvmStatic
    fun fromRequest(value: Request) = value.tag

    @TypeConverter
    @JvmStatic
    fun toRequest(value: String) = requestValues.firstOrNull { it.tag == value }
}