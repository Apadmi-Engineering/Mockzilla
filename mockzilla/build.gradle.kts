import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.swiftklib)
    id("maven-publish")
    id("publication-convention")
}

kotlin {
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
            baseName = "mockzilla"
            xcf.add(this)
        }
        it.compilations {
            val main by getting {
                cinterops {
                    create("LocalDiscovery")
                }
            }
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
            api(project(":mockzilla-common"))

            /* Ktor */
            api(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.server.rate.limit)

            /* Serialization */
            implementation(libs.kotlinx.serialization.json)

            /* Logging */
            implementation(libs.kermit)

            /* Date Time */
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.mockative)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

swiftklib {
    create("LocalDiscovery") {
        path = file("native-darwin/LocalDiscovery")
        packageName("com.apadmi.mockzilla.lib.nativedarwin.localdiscovery")
    }
}

android {
    namespace = group.toString()
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

buildkonfig {
    packageName = "$group.mockzilla"

    defaultConfigs {
        buildConfigField(STRING, "VERSION_NAME", version.toString())
    }
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.mockative.processor)
        }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Mockzilla")
            description.set("Solution for running and configuring a local HTTP server on mobile.")
        }
    }
}