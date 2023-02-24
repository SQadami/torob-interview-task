package ir.torob.db.room.dao

import ir.torob.data.model.Entry
import ir.torob.data.model.EntryWithProduct
import ir.torob.db.dao.EntryDao

/**
 * This interface represents a DAO which contains entities which are part of a single collective list.
 */
interface RoomEntryDao<EC : Entry, LI : EntryWithProduct<EC>> : EntryDao<EC, LI>, RoomEntityDao<EC>