package ir.torob.core

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

private const val IS_LAUNCHED = "appData_isLaunched"

fun initAppData(application: Application) {
    application.registerActivityLifecycleCallbacks(DataLifecycleCallback())
}

object AppData {

    var isLaunched: Boolean = true
}

private class DataLifecycleCallback : ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        if (bundle != null) restoreAppDataState(AppData, bundle)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        if (activity == currentActivity) saveAppDataState(AppData, bundle)
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity == currentActivity) currentActivity = null
    }

    private fun saveAppDataState(appData: AppData, bundle: Bundle) {
        bundle.putBoolean(IS_LAUNCHED, appData.isLaunched)
    }

    private fun restoreAppDataState(appData: AppData, bundle: Bundle) {
        if (bundle.containsKey(IS_LAUNCHED)) {
            appData.isLaunched = bundle.getBoolean(IS_LAUNCHED)
        }
    }
}