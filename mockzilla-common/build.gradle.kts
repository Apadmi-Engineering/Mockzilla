import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("publication-convention")
}

kotlin {
    // Managed automatically by release-please PRs
    version = "2.0.0" // x-release-please-version
    androidTarget {
        publishAllLibraryVariants()
    }

    val xcf = XCFramework()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "mockzilla-common"
            xcf.add(this)
        }
    }

    jvm()
    jvmToolchain(JavaConfig.toolchain)

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        commonMain.dependencies {
            /* Kotlin */
            implementation(libs.kotlinx.coroutines.core)

            /* Ktor */
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.server.core)

            /* Serialization */
            implementation(libs.kotlinx.serialization.json)

            /* Date Time */
            implementation(libs.kotlinx.datetime)

            /* Logging */
            implementation(libs.kermit)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.mockative)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "$group.mockzilla.common"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk

        consumerProguardFiles("mockzilla-proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("mockzilla-common")
            description.set(
                """
                A utility module containing common utilities and models used by multiple different mockzilla libraries.
            """.trimIndent()
            )
        }
    }
}