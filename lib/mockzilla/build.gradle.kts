import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

import java.util.Date

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("maven-publish")
    id("com.google.devtools.ksp")
    id("com.codingfeline.buildkonfig")
    id("publication-convention")
}

group = "com.apadmi"
version = extractVersion()

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

        jvmToolchain(17)

        val commonMain by getting {
            dependencies {
                /* Kotlin */
                implementation(libs.kotlinx.coroutines.core)

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
        val jvmMain by getting
        val jvmTest by getting
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

tasks.dokkaHtml {
    outputDirectory.set(File(System.getProperty("docsOutputDirectory", "temp")))
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, "io.mockative:mockative-processor:1.2.3")
        }
}

val debugVersionFile get() = File("${project.rootProject.projectDir.parent}/debug-version.txt")
val versionFile get() = File("${project.rootProject.projectDir.parent}/version.txt")

tasks.getByPath("publishToMavenLocal").dependsOn(
    tasks.register("updateDebugMockzillaVersion" ) {
        val newVersion = versionFile.readText().trim() + "-${Date().toInstant().epochSecond}"
        version = newVersion
        debugVersionFile.writeText(newVersion)
    }
)

fun extractVersion(): String {
    return debugVersionFile
        .takeIf { it.exists() }
        ?.readText()
        ?.trim()?.takeUnless { it.isBlank() } ?: versionFile.readText().trim()
}