@file:Suppress("DEPRECATION")

package ir.torob.core.extension

import android.content.Context
import android.os.Build

fun Context.installPackageName() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        packageManager.getInstallSourceInfo(packageName).installingPackageName ?: "unknown"
    } else {
        packageManager.getInstallerPackageName(packageName) ?: "unknown"
    }