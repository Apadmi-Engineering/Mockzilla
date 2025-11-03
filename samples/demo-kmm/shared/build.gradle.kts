import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
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

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            /* Compose */
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.resources)

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
