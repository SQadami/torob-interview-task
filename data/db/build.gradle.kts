@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ir.torob.db"
}

room {
    schemaLocationDir.set(file("$projectDir/schemas"))
}

dependencies {

    api(projects.data.model)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
    api(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)

    api(libs.androidx.paging.runtime)
}