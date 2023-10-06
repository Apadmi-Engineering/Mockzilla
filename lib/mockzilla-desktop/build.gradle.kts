import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.spotless) apply true
}

kotlin {
    jvm {
        jvmToolchain(JavaConfig.toolchain)
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                /* Compose */
                implementation(compose.desktop.currentOs) {
                    exclude("org.jetbrains.compose.material")
                }
                implementation(libs.material.icons)
                implementation(libs.material3)

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.swing)

                /* DI */
                implementation(libs.koin.core)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mockzilla-desktop"
            packageVersion = "1.0.0"
        }
    }
}
