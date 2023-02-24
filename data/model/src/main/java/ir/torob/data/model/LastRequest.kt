package ir.torob.data.model

import androidx.room.*
import kotlinx.datetime.Instant

@Entity(
    tableName = "last_requests",
    indices = [Index(value = ["request", "entity_id"], unique = true)],
)
data class LastRequest(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "request") val request: Request,
    @ColumnInfo(name = "entity_id") val entityId: String,
    // We have to use a raw Long type here rather than Timestamp. This is because Timestamp is
    // currently mapped to a string (by type converters) for legacy reasons. For the same reason,
    // the old Instant type converter mapped to an int sql type, meaning that we can use the
    // same type converter for pre-existing data.
    @ColumnInfo(name = "timestamp") internal val _timestamp: Long,
) : ProductEntity {
    @delegate:Ignore
    val timestamp: Instant by lazy { Instant.fromEpochMilliseconds(_timestamp) }
}