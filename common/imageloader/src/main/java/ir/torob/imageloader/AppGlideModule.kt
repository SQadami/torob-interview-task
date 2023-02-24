package ir.torob.imageloader

import android.content.Context
import android.os.Debug
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import ir.torob.imageloader.network.OkHttpUrlLoader
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class AppGlideModule : com.bumptech.glide.module.AppGlideModule() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface AppGlideModuleEntryPoint {
        fun defaultOkHttpClient(): OkHttpClient
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val appContext: Context = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            AppGlideModuleEntryPoint::class.java
        )
        val client = entryPoint.defaultOkHttpClient()

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        if (Debug.isDebuggerConnected()) {
            builder.setLogLevel(Log.DEBUG)
        }
    }
}