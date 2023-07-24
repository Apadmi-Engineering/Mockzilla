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

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        val ktorVersion = "2.3.0"
        val commonMain by getting {
            dependencies {
                /* Kotlin */
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

                /* Ktor */
                api("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-server-rate-limit:$ktorVersion")

                /* Serialization */
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")

                /* Logging */
                implementation("co.touchlab:kermit:1.2.2")

                /* Date Time */
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("io.mockative:mockative:1.2.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        val androidMain by getting
        val androidTest by getting
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

val debugVersionFile get() = File("${project.rootProject.projectDir.parent}/debug-version")
val versionFile get() = File("${project.rootProject.projectDir.parent}/version")

tasks.getByPath("publishToMavenLocal").dependsOn(
    tasks.register("updateDebugMockzillaVersion" ) {
        val newVersion = versionFile.readText() + "-${Date().toInstant().epochSecond}"
        version = newVersion
        debugVersionFile.writeText(newVersion)
    }
)

fun extractVersion(): String {
    return debugVersionFile
        .takeIf { it.exists() }
        ?.readText()
        ?.takeUnless { it.isBlank() } ?: versionFile.readText()
}