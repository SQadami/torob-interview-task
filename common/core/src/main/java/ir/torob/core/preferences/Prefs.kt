@file:Suppress("JoinDeclarationAndAssignment", "SpellCheckingInspection", "PrivatePropertyName")

package ir.torob.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class Prefs(prefsName: String, context: Context) {

    private lateinit var ANDX_SECURITY_KEY_KEYSET: String
    private lateinit var ANDX_SECURITY_VALUE_KEYSET: String
    private lateinit var cntext: Context
    private lateinit var prefName: String

    private lateinit var prefs: SharedPreferences

    constructor(
        prefsName: String,
        context: Context,
        masterKeyAlias: MasterKey,
        prefKeyEncryptionScheme: EncryptedSharedPreferences.PrefKeyEncryptionScheme,
        prefValueEncryptionScheme: EncryptedSharedPreferences.PrefValueEncryptionScheme
    ) : this(prefsName, context) {
        ANDX_SECURITY_KEY_KEYSET = "__androidx_security_crypto_encrypted_prefs_key_keyset__"
        ANDX_SECURITY_VALUE_KEYSET = "__androidx_security_crypto_encrypted_prefs_value_keyset__"
        cntext = context
        prefName = prefsName
        prefs =
            EncryptedSharedPreferences.create(
                context,
                prefsName,
                masterKeyAlias,
                prefKeyEncryptionScheme,
                prefValueEncryptionScheme
            )
    }

    init {
        if (!::ANDX_SECURITY_KEY_KEYSET.isInitialized) {
            prefs =
                context.getSharedPreferences(
                    prefsName,
                    Context.MODE_PRIVATE
                )
        }
    }

    companion object {

        const val INVALID_BOOLEAN: Boolean = false
        const val INVALID_FLOAT: Float = -11111111111F
        const val INVALID_INT: Int = -1111111111
        const val INVALID_LONG: Long = -11111111111L
        const val INVALID_STRING: String = "INVALID_STRING"

        val INVALID_STRING_SET: Set<String> = setOf(INVALID_STRING)


        fun initEncryptedPrefs(prefsName: String, context: Context) =
            Prefs(
                prefsName,
                context,
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        fun initPrefs(prefsName: String, context: Context) = Prefs(prefsName, context)
    }

    /**
     * OnChangeListener
     * */
    fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) =
        prefs.registerOnSharedPreferenceChangeListener(listener)

    fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) =
        prefs.unregisterOnSharedPreferenceChangeListener(listener)

    /**
     * Read Shared Prefs
     * */
    fun contains(key: String): Boolean =
        prefs.contains(key)

    fun getAll(): Map<String, *> =
        prefs.all

    /**
     * read Boolean from sharedPreferences
     * Returns null if the Boolean value is not in
     * */
    fun read(key: String): Boolean? =
        if (contains(key)) {
            read(key, INVALID_BOOLEAN)
        } else {
            null
        }

    /**
     * read Boolean from sharedPreferences
     * */
    fun read(key: String, returnIfInvalid: Boolean): Boolean =
        prefs.getBoolean(key, returnIfInvalid)

    /**
     * read Float from sharedPreferences
     * */
    fun read(key: String, returnIfInvalid: Float): Float =
        prefs.getFloat(key, returnIfInvalid)

    /**
     * read Int from sharedPreferences
     * */
    fun read(key: String, returnIfInvalid: Int): Int =
        prefs.getInt(key, returnIfInvalid)

    /**
     * read Long from sharedPreferences
     * */
    fun read(key: String, returnIfInvalid: Long): Long =
        prefs.getLong(key, returnIfInvalid)

    /**
     * read String from sharedPreferences
     * */
    fun read(key: String, returnIfInvalid: String): String? =
        prefs.getString(key, returnIfInvalid)

    /**
     * read Set<String> from sharedPreferences
     * */
    fun read(key: String, returnIfInvalid: Set<String>): Set<String>? =
        prefs.getStringSet(key, returnIfInvalid)

    /**
     * Modify Shared Prefs
     * */
    fun clear() {
        if (::ANDX_SECURITY_KEY_KEYSET.isInitialized) {
            val clearTextPrefs = cntext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val keyKeyset = clearTextPrefs.getString(ANDX_SECURITY_KEY_KEYSET, INVALID_STRING)
            val valueKeyset = clearTextPrefs.getString(ANDX_SECURITY_VALUE_KEYSET, INVALID_STRING)
            if (keyKeyset != null && keyKeyset != INVALID_STRING
                && valueKeyset != null && valueKeyset != INVALID_STRING
            ) {
                if (!clearTextPrefs.edit().clear().commit()) {
                    clearTextPrefs.edit().clear().apply()
                }
                if (
                    !clearTextPrefs.edit()
                        .putString(ANDX_SECURITY_KEY_KEYSET, keyKeyset)
                        .commit()
                ) {
                    clearTextPrefs.edit()
                        .putString(ANDX_SECURITY_KEY_KEYSET, keyKeyset)
                        .apply()
                }
                if (
                    !clearTextPrefs.edit()
                        .putString(ANDX_SECURITY_VALUE_KEYSET, valueKeyset)
                        .commit()
                ) {
                    clearTextPrefs.edit()
                        .putString(ANDX_SECURITY_VALUE_KEYSET, valueKeyset)
                        .apply()
                }
            }
        } else {
            if (!prefs.edit().clear().commit()) {
                prefs.edit().clear().apply()
            }
        }
    }

    fun remove(key: String) {
        if (!prefs.edit().remove(key).commit()) {
            prefs.edit().remove(key).apply()
        }
    }

    /**
     * write Boolean into sharedPreferences
     * */
    fun write(key: String, value: Boolean) {
        if (!prefs.edit().putBoolean(key, value).commit()) {
            prefs.edit().putBoolean(key, value).apply()
        }
    }

    /**
     * write Float into sharedPreferences
     * */
    fun write(key: String, value: Float) {
        if (!prefs.edit().putFloat(key, value).commit()) {
            prefs.edit().putFloat(key, value).apply()
        }
    }

    /**
     * write Int into sharedPreferences
     * */
    fun write(key: String, value: Int) {
        if (!prefs.edit().putInt(key, value).commit()) {
            prefs.edit().putInt(key, value).apply()
        }
    }

    /**
     * write Long into sharedPreferences
     * */
    fun write(key: String, value: Long) {
        if (!prefs.edit().putLong(key, value).commit()) {
            prefs.edit().putLong(key, value).apply()
        }
    }

    /**
     * write String into sharedPreferences
     * */
    fun write(key: String, value: String) {
        if (!prefs.edit().putString(key, value).commit()) {
            prefs.edit().putString(key, value).apply()
        }
    }

    /**
     * write Set<String> into sharedPreferences
     * */
    fun write(key: String, value: Set<String>) {
        if (!prefs.edit().putStringSet(key, value).commit()) {
            prefs.edit().putStringSet(key, value).apply()
        }
    }
}