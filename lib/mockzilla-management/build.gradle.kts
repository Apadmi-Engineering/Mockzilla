import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.extractVersion
import com.apadmi.mockzilla.ProjectConfig

plugins {
    kotlin("multiplatform")
}

group = ProjectConfig.group
version = extractVersion()

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(JavaConfig.toolchain)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                /* Kotlin */
                implementation(libs.kotlinx.coroutines.core)


                /* Ktor */
                api(libs.ktor.server.core)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.resources)
                implementation(libs.ktor.client.logging)

                /* Serialization */
                implementation(libs.kotlinx.serialization.json)

                /* Logging */
                implementation(libs.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                /* Mockzilla */
                implementation(project(":mockzilla"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
    }
}
