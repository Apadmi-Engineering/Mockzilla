import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
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
            add(it.name, libs.mockative.processor)
        }
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