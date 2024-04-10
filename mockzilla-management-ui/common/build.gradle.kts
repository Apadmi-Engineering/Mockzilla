import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
}

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(JavaConfig.toolchain)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                /* Compose */
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.preview)

                /* Localisable Strings */
                implementation(libs.lyricist.library)

                /* DI */
                implementation(libs.koin.core)

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.showkase)

                /* Mockzilla Management */
                implementation(project(":mockzilla-management"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                /* Compose */
                implementation(libs.androidx.compose.ui)
                implementation(libs.androidx.compose.activity)
                implementation(libs.androidx.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycleRuntimeKtx)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.compose.materialIconsExt)

                /* ViewModel */
                implementation(libs.androidx.lifecycleViewModelCompose)
                implementation(libs.koin.android)
                implementation(libs.koin.compose)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.androidx.test.junit)
                implementation(libs.testParamInjector)
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
        val desktopTest by getting {
            dependencies {
                implementation(libs.turbine)
                implementation(libs.mockative)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

dependencies {
    "kspAndroid"(libs.showkase.processor)

    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, "io.mockative:mockative-processor:2.0.1")
        }
}

android {
    namespace = group.toString()
    compileSdk = AndroidConfig.targetSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidConfig.minSdk
    }
    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}