import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "$group.mockzilla.kmm.shared"
        compileSdk = AndroidConfig.targetSdk
        minSdk = AndroidConfig.minSdk
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
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
