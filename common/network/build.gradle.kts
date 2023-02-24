@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ir.torob.network"
}

dependencies {
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.androidx.lifecycle.livedata)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    api(libs.okhttp.okhttp)
    api(libs.okhttp.loggingInterceptor)
    api(libs.retrofit.retrofit)
    api(libs.retrofit.moshiConverter)
    api(libs.moshi)
}