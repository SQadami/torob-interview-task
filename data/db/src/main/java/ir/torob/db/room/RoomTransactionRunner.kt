package ir.torob.db.room

import androidx.room.withTransaction
import ir.torob.db.DatabaseTransactionRunner

class RoomTransactionRunner(
    private val db: ProductRoomDatabase,
) : DatabaseTransactionRunner {
    override suspend operator fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction {
            block()
        }
    }
}