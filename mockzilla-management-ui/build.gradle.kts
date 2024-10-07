import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.app)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
    alias(libs.plugins.conveyor)
}

kotlin {
    androidTarget()
    jvmToolchain(JavaConfig.toolchain)
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            /* Compose */
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.preview)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            /* Localisable Strings */
            implementation(libs.lyricist.library)

            /* DI */
            implementation(libs.koin.core)

            /* Coroutines */
            implementation(libs.kotlinx.coroutines.core)

            /* JSON */
            implementation(libs.kotlinx.serialization.json)

            /* Mockzilla Management */
            implementation(project(":mockzilla-management"))

            /* ADB */
            implementation(libs.adam)

            /* Zeroconf */
            implementation(libs.jmdns.jmdns)

            /* Logging */
            implementation(libs.kermit)

            /* Version Handling */
            implementation(libs.semver4j)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            /* Compose */
            implementation(libs.showkase)

            /* ViewModel */
            implementation(libs.androidx.lifecycleViewModelCompose)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)

            implementation(libs.androidx.compose.activity)

            /* Mockzilla */
            // Android target is only used for development since it's a better dev experience than desktop
            // So using mockzilla to have a "Mock app" to connect to
            implementation(project(":mockzilla"))
            implementation(libs.ktor.client.core)
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

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.mockk)
            }
        }
    }
}

dependencies {
    "kspAndroid"(libs.showkase.processor)
}

// TODO Remove when https://github.com/google/guava/issues/6567 is fixed.
// See also: https://github.com/google/guava/issues/6801.
// Paparazzi issue here: https://github.com/cashapp/paparazzi/issues/1231
dependencies.constraints {
    testImplementation("com.google.guava:guava") {
        attributes {
            attribute(
                TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                objects.named(TargetJvmEnvironment::class.java, TargetJvmEnvironment.STANDARD_JVM)
            )
        }
        because("Paparazzi's layoutlib and sdk-common depend on Guava's -jre published variant." +
                "See https://github.com/cashapp/paparazzi/issues/906.")
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

android {
    // Managed automatically by release-please PRs
    version = "1.0.0" // x-release-please-version
    compileSdk = AndroidConfig.targetSdk
    namespace = group.toString()
    defaultConfig {
        applicationId = group.toString()
        minSdk = AndroidConfig.minSdk
        versionCode = System.getProperties().getOrDefault("versionCode", "1").toString().toInt()
        versionName = versionName
    }
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    android {
        packaging {
            exclude("META-INF/*")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mockzilla-management-ui"

            jvmArgs("-Dapple.awt.application.appearance=system", "-Djava.net.preferIPv4Stack=true")
        }
    }
}

dependencies {
    // Use the configurations created by the Conveyor plugin to tell Gradle/Conveyor where to find the artifacts for each platform.
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

configurations.all {
    attributes {
        // Temporary fix for https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
