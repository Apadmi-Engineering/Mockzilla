pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "lib"
includeBuild("build-logic")
include(":mockzilla-management")
include(":mockzilla-common")
include(":mockzilla")
