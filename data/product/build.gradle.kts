@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ir.torob.product"
}

dependencies {

    api(projects.data.model)
    api(projects.common.network)
    implementation(projects.data.db)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    api(libs.store)
    implementation(libs.kotlinx.atomicfu)
}