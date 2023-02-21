package ir.torob.core.extension

inline fun <reified T : Enum<T>> valueOf(type: String): T? {
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T : Enum<T>> valueOf(type: String, default: T): T = valueOf<T>(type) ?: default