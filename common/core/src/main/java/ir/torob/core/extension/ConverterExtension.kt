package ir.torob.core.extension

fun String.safeLong() =
    try {
        toLong()
    } catch (e: Exception) {
        null
    }

fun String.safeInt() =
    try {
        toInt()
    } catch (e: Exception) {
        null
    }