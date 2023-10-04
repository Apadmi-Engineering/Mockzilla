
import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {

    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("publication-convention")
    id("com.android.library")
}

kotlin {
    android {
        publishAllLibraryVariants()
    }

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "mockzilla"
            xcf.add(this)
        }
    }

    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        jvmToolchain(JavaConfig.toolchain)

        val commonMain by getting {
            dependencies {
                /* Kotlin */
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)

                /* Serialization */
                implementation(libs.kotlinx.serialization.json)

                /* Date Time */
                implementation(libs.kotlinx.datetime)

                /* Logging */
                implementation(libs.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation(libs.mockative)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        val androidMain by getting
        val androidTest by getting
        val jvmMain by getting {
            dependsOn(commonMain)
        }
        val jvmTest by getting {
            dependsOn(commonTest)
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32

        consumerProguardFiles("mockzilla-proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}

tasks.dokkaHtml {
    outputDirectory.set(File(System.getProperty("docsOutputDirectory", "temp")))
}

