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
include(":mockzilla-management-ui:desktop")
include(":mockzilla-management-ui:common")
include(":mockzilla-management-ui:android")
include(":mockzilla-common")
include(":mockzilla")
