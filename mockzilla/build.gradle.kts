import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.injectedVersion
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildKonfig)
    id("maven-publish")
    id("publication-convention")
    kotlin("native.cocoapods") apply true
}

kotlin {
    androidTarget {
        publishAllLibraryVariants()
    }

    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "2.0.1" // x-release-please-version

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
    jvmToolchain(JavaConfig.toolchain)
    cocoapods {
        name = "SwiftMockzilla"
        summary = "A solution for running and configuring a local HTTP server to mimic REST API endpoints used by your application."
        homepage = "https://apadmi-engineering.github.io/Mockzilla/"
        framework {
            baseName = "mockzilla"
        }
        license = "{:type => 'MIT', :file => 'LICENSE'}"
        source = "{ :git => 'https://github.com/Apadmi-Engineering/SwiftMockzilla.git', :tag => 'v$version' }"
        extraSpecAttributes["vendored_frameworks"] = "'Mockzilla.xcframework'"
        extraSpecAttributes["source_files"] = "'Sources/SwiftMockzilla/SwiftMockzilla.swift'"
        extraSpecAttributes["swift_version"] = "'5.9.2'"

        ios.deploymentTarget = "13.0"
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        androidMain.dependencies {
            implementation(libs.play.services.ads.identifier)
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

            implementation(libs.kotlinx.coroutines.test)
        }
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
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")


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
private val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Mockzilla")
            description.set("Solution for running and configuring a local HTTP server on mobile.")
        }
    }

    publications.filterIsInstance<MavenPublication>().forEach {
        it.artifact(javadocJar);
    }
}