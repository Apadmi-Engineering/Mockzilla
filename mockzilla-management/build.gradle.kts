import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.injectedVersion

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("publication-convention")
}

repositories {
    mavenCentral()
}

kotlin {
    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "2.1.0" // x-release-please-version

    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    jvmToolchain(JavaConfig.toolchain)

    sourceSets {
        commonMain.dependencies {
            /* Kotlin */
            implementation(libs.kotlinx.coroutines.core)

            /* Common Mockzilla */
            api(project(":mockzilla-common"))

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
        commonTest.dependencies {
            implementation(kotlin("test"))

            /* Mockzilla */
            implementation(project(":mockzilla"))
        }
    }
}

private val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("mockzilla-management")
            description.set(
                """
                A library that provides a kotlin interface to interact with the Mockzilla server 
                running on device. This is used by the Mockzilla dashboard ui internally.
            """.trimIndent()
            )
        }
    }
    publications.filterIsInstance<MavenPublication>().forEach {
        it.artifact(javadocJar);
    }
}