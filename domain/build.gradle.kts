@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ir.torob.domain"
}

// https://github.com/cashapp/multiplatform-paging/issues/6
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>> {
    compilerOptions.freeCompilerArgs.add("-opt-in=androidx.paging.ExperimentalPagingApi")
}

dependencies {

    api(projects.common.core)
    api(projects.data.model)
    api(projects.data.product)
    implementation(projects.data.db)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    api(libs.cashapp.paging.common)

    api(libs.androidx.room.common) // Room annotations
}