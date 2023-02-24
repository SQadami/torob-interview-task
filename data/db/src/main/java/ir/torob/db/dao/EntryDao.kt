package ir.torob.db.dao

import ir.torob.data.model.Entry
import ir.torob.data.model.EntryWithProduct

/**
 * This interface represents a DAO which contains entities which are part of a single collective list.
 */
interface EntryDao<EC : Entry, LI : EntryWithProduct<EC>> : EntityDao<EC> {
    suspend fun deleteAll()
}