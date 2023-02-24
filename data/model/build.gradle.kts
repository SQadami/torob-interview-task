@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ir.torob.model"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>> {
    compilerOptions.allWarningsAsErrors.set(false)
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
//    compilerOptions {
//        // Treat all Kotlin warnings as errors
//        allWarningsAsErrors.set(fal)
//
//        // Enable experimental coroutines APIs, including Flow
//        freeCompilerArgs.addAll(
//            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//            "-opt-in=kotlinx.coroutines.FlowPreview",
//        )
//    }
//}

dependencies {

    api(libs.androidx.room.common) // Room annotations

    api(libs.kotlinx.datetime)
}