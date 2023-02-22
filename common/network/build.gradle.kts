@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.cacheFixPlugin)
}

android {
    namespace = "ir.torob.network"
}

dependencies {
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.androidx.lifecycle.livedata)

    api(libs.okhttp.okhttp)
    api(libs.okhttp.loggingInterceptor)
    api(libs.retrofit.retrofit)
    api(libs.retrofit.moshiConverter)
}