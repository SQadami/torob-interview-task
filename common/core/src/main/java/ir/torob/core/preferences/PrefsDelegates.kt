package ir.torob.core.preferences

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringPreference(
    private val sharedPreferences: Prefs,
    private val key: String,
    private val defaultValue: String = Prefs.INVALID_STRING
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        sharedPreferences.read(key, defaultValue) ?: defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        sharedPreferences.write(key, value)
    }
}

class BooleanPreference(
    private val sharedPreferences: Prefs,
    private val key: String,
    private val defaultValue: Boolean = Prefs.INVALID_BOOLEAN
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        sharedPreferences.read(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        sharedPreferences.write(key, value)
}

class IntPreference(
    private val sharedPreferences: Prefs,
    private val key: String,
    private val defaultValue: Int = Prefs.INVALID_INT
) : ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        sharedPreferences.read(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        sharedPreferences.write(key, value)
}

class LongPreference(
    private val sharedPreferences: Prefs,
    private val key: String,
    private val defaultValue: Long = Prefs.INVALID_LONG
) : ReadWriteProperty<Any, Long> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Long =
        sharedPreferences.read(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) =
        sharedPreferences.write(key, value)
}

class FloatPreference(
    private val sharedPreferences: Prefs,
    private val key: String,
    private val defaultValue: Float = Prefs.INVALID_FLOAT
) : ReadWriteProperty<Any, Float> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Float =
        sharedPreferences.read(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) =
        sharedPreferences.write(key, value)
}