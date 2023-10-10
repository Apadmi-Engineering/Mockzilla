plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    
    listOf( 
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                /* Mockzilla */
                api("com.apadmi:mockzilla:${extractMockzillaVersion()}")

                /* Json parsing */
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")

                /* Networking */
                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("io.ktor:ktor-client-cio:2.3.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
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
    namespace = "com.apadmi.mockzilla.mock"
    compileSdk = 33
    defaultConfig {
        minSdk = 23
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

fun extractMockzillaVersion(): String {
    val version = providers.fileContents(
        rootProject.layout.projectDirectory.file("version.txt")
    ).asText.get().trim()

    val debugVersionFile = rootProject.layout.projectDirectory.file("debug-version.txt")
    val debugVersion = debugVersionFile.takeIf { it.asFile.exists() }?.let {
        providers.fileContents(it).asText.orNull
    }?.trim()?.takeUnless { it.isBlank() }

    return debugVersion ?: version
}