import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.configureCommonProperties
import com.apadmi.mockzilla.isSigningEnabled
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktechPublish)
}

repositories {
    mavenCentral()
}

val artifactName = "mockzilla-management"

kotlin {
    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "2.4.1" // x-release-please-version

    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = artifactName
            xcf.add(this)
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

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (isSigningEnabled()) {
        signAllPublications()
    }

    coordinates(group.toString(), artifactName, version.toString())

    pom {
        name.set(artifactName)
        description.set("""
            A library that provides a kotlin interface to interact with the Mockzilla server
            running on device. This is used by the Mockzilla dashboard ui internally.
        """.trimIndent())

        configureCommonProperties()
    }
}
