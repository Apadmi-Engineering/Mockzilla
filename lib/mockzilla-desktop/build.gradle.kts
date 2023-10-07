import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    alias(libs.plugins.compose)
    alias(libs.plugins.spotless) apply true
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {

            dependencies {

                /* DI */
                implementation(libs.koin.core)

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val desktopMain by getting {
            dependencies {
                /* Compose */
                implementation(compose.desktop.currentOs) {
                    exclude("org.jetbrains.compose.material")
                }

                implementation(libs.desktop.compose.material.icons)
                implementation(libs.desktop.compose.material3)

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.swing)
            }
        }

        val androidMain by getting {
            dependencies {
                /* Compose */
                implementation(libs.androidx.compose.ui)
                implementation(libs.androidx.compose.material)
                implementation(libs.androidx.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycleRuntimeKtx)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.compose.materialIconsExt)

                /* ViewModel */
                implementation(libs.androidx.lifecycleViewModelCompose)
            }
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mockzilla-desktop"
            packageVersion = version.toString().split("-").first()
        }
    }
}
