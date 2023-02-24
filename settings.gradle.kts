pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// https://docs.gradle.org/7.6/userguide/configuration_cache.html#config_cache:stable
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
    ":app",
    ":data:model",
    ":data:db",
    ":data:product",
    ":domain",
    ":common:core",
    ":common:ui",
    ":common:network",
    ":common:imageloader"
)

rootProject.name = "torobSampleApp"
