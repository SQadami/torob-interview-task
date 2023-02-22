@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ir.torob.core"
}

dependencies {
    implementation(libs.javax.inject)

    implementation(libs.kotlin)
    implementation(libs.kotlin.reflect)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.common)
    kapt(libs.androidx.lifecycle.compile)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.securityCypto)

    implementation(libs.androidx.fragmentKtx)
}