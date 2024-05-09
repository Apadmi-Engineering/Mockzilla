import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android()
    
    listOf( 
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                /* Mockzilla */
                api(project(":mockzilla"))

                /* Json parsing */
                implementation(libs.kotlinx.serialization.json)

                /* Networking */
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "$group.mockzilla.kmm.shared"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk

        compileOptions {
            sourceCompatibility = JavaConfig.version
            targetCompatibility = JavaConfig.version
        }
    }
}
