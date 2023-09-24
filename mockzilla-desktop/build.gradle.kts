import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
}

group = "com.apadmi.mockzilla.desktop"
version = "0.0.0"

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                /* Compose */
                implementation(compose.desktop.currentOs)
                implementation(libs.material.icons)
                implementation(libs.material3)

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
