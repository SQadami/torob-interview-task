package ir.torob.sample

import android.annotation.SuppressLint
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ir.torob.core.initAppData

@HiltAndroidApp
class TorobSampleApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: TorobSampleApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        initAppData(this)
    }
}