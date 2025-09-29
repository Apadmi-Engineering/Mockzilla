pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

rootProject.name = "lib"
includeBuild("build-logic")
include(":mockzilla-management")
include(":samples:demo-android")
include(":samples:demo-kmm:androidApp")
include(":samples:demo-kmm:shared")
include(":mockzilla-common")
include(":mockzilla")
include(":mockzilla-management-ui:mockzilla-management-ui-common")
include(":mockzilla-management-ui:mockzilla-desktop")
include(":mockzilla-management-ui:mockzilla-mobile-ui")
