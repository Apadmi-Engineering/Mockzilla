pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "lib"
includeBuild("build-logic")
include(":mockzilla-management")
include(":mockzilla-management-ui")
include(":samples:demo-android")
include(":samples:demo-kmm:androidApp")
include(":samples:demo-kmm:shared")
include(":mockzilla-common")
include(":mockzilla")
