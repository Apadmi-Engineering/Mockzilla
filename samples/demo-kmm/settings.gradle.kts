pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()

        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "demo-kmm"
include(":androidApp")
include(":shared")