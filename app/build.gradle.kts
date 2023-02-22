@file:Suppress(
    "DSL_SCOPE_VIOLATION",
    "UnstableApiUsage",
    "UNCHECKED_CAST"
)

import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.cacheFixPlugin)
}

val appVersionCode = propOrDef("APP_VERSION_CODE", "1").toInt()
println("APK version code: $appVersionCode")
val appVersionName = propOrDef("APP_VERSION_NAME", "1.0.0")
println("APK version name: $appVersionName")

android {
    namespace = "ir.torob.sample"

    defaultConfig {
        applicationId = "ir.torob.sample"
        versionCode = appVersionCode
        versionName = appVersionName
    }

    lint {
        baseline = file("lint-baseline.xml")
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds = false
        // Ignore any tests
        ignoreTestSources = true
        // Make the build fail on any lint errors
        abortOnError = true
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    packagingOptions {
        packagingOptions.resources.excludes += setOf(
            // Exclude AndroidX version files
            "META-INF/*.version",
            // Exclude consumer proguard files
            "META-INF/proguard/*",
            // Exclude random properties files
            "/*.properties",
            "META-INF/*.properties",
        )
    }

    buildTypes {
        debug {
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".debug"
        }

        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
}

tasks.withType<KotlinCompilationTask<*>> {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi")
    }
}

dependencies {

    implementation(projects.common.core)
    implementation(projects.common.ui)
    implementation(projects.common.imageloader)

    implementation(libs.androidx.navigation)
    implementation(libs.androidx.navigation.fragment)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.moshi)
    kapt(libs.moshi.codegen)
}

fun <T : Any> propOrDef(propertyName: String, defaultValue: T): T {
    val propertyValue = project.properties[propertyName] as T?
    return propertyValue ?: defaultValue
}
