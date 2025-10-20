import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }
    jvmToolchain(JavaConfig.toolchain)

    sourceSets {
        commonMain.dependencies {
            /* Mockzilla */
            api(project(":mockzilla"))
            api(project(":mockzilla-management-ui:mockzilla-mobile-ui"))

            /* Json parsing */
            implementation(libs.kotlinx.serialization.json)

            /* Networking */
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("com.apadmi:mockzilla-management"))
            .using(project(":mockzilla-management"))
        substitute(module("com.apadmi:mockzilla-common"))
            .using(project(":mockzilla-common"))
    }
}

android {
    namespace = "$group.mockzilla.kmm.shared"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        testOptions.targetSdk = AndroidConfig.targetSdk

        compileOptions {
            sourceCompatibility = JavaConfig.version
            targetCompatibility = JavaConfig.version
        }
    }
}
