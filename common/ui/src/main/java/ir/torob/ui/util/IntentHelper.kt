package ir.torob.ui.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import ir.torob.ui.extension.stringRes
import java.util.*

fun Fragment.share(
    toShareText: String,
    chooserTitleRes: Int,
    shareIntent: Intent = Intent()
) {
    shareIntent.run {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, toShareText)
    }
    val intent = Intent.createChooser(shareIntent, chooserTitleRes.stringRes(requireContext()))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    requireContext().startActivity(intent)
}

fun Fragment.openUrl(url: String) {
    val uri = Uri.parse(url)
    runCatching {
        openChooser(requireContext(), uri)
    }
}

fun Fragment.openMarket(downloadUrl: String): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        intent.data = Uri.parse("market://details?id=" + requireContext().packageName)
        this.startActivity(intent)
        return true
    } catch (e: ActivityNotFoundException) {
        openUrl(downloadUrl)
        Log.e("VersionUpdate", e.message + "no available market installed")
    }
    return false
}

private fun openChooser(context: Context, url: Uri) {
    val i = Intent(Intent.ACTION_VIEW, url)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
    val finalIntent = chooserIntent(context, i, url)
    if (finalIntent != null) {
        try {
            context.startActivity(finalIntent)
        } catch (ignored: ActivityNotFoundException) {
        }
    } else {
        try {
            context.startActivity(i)
        } catch (ignored: ActivityNotFoundException) {
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
@Suppress("Deprecation")
private fun chooserIntent(context: Context, intent: Intent, uri: Uri): Intent? {
    val pm = context.packageManager
    val activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    val chooserIntents = ArrayList<Intent>()
    val ourPackageName = context.packageName
    for (resInfo in activities) {
        val info = resInfo.activityInfo
        if (!info.enabled || !info.exported) {
            continue
        }
        if (info.packageName == ourPackageName) {
            continue
        }
        val targetIntent = Intent(intent)
        targetIntent.setPackage(info.packageName)
        targetIntent.setDataAndType(uri, intent.type)
        chooserIntents.add(targetIntent)
    }
    if (chooserIntents.isEmpty()) {
        return null
    }
    val lastIntent = chooserIntents.removeAt(chooserIntents.size - 1)
    if (chooserIntents.isEmpty()) {
        return lastIntent
    }
    val chooserIntent = Intent.createChooser(lastIntent, null)
    chooserIntent.putExtra(
        Intent.EXTRA_INITIAL_INTENTS,
        chooserIntents.toTypedArray()
    )
    return chooserIntent
}