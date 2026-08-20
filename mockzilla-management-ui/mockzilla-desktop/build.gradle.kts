import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.CompilerConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.githubToken
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.isDevelopmentBuild
import com.apadmi.mockzilla.isSnapshot
import com.apadmi.mockzilla.runNumber
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import kotlin.math.sign

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
    alias(libs.plugins.conveyor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.dokka) apply true
}

val artifactName = "mockzilla-management-ui"

// Managed automatically by release-please PRs
val baseVersion = "3.0.0" // x-release-please-version

kotlin {
    // In the desktop world there's no concept of a build number so we have to bump the actual version
    // for each snapshot, so we replace the patch with the github run number just for snapshots
    version = runNumber()?.takeIf { isSnapshot() }?.let {
        // Max patch number is 65535, since we're unlikely to have this many builds per version
        // we just let it loop
        baseVersion
            .split(".")
            .dropLast(1)
            .joinToString(".") + ".${it % 65535}"
    } ?: baseVersion

    android {
        namespace = "$group.mockzilla.desktop"
        compileSdk = AndroidConfig.targetSdk
        minSdk = 26
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        withHostTest {}
    }
    jvmToolchain(JavaConfig.toolchain)
    jvm("desktop")

    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        commonMain.dependencies {
            /* Compose */
            implementation(libs.runtime)
            implementation(libs.material3)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.navigation.compose)
            implementation(libs.navigation.compose)

            /* Localisable Strings */
            implementation(libs.lyricist.library)

            /* DI */
            implementation(libs.koin.core)

            /* Coroutines */
            implementation(libs.kotlinx.coroutines.core)

            /* JSON */
            implementation(libs.kotlinx.serialization.json)

            /* Mockzilla */
            api(project(":mockzilla-management-ui:mockzilla-management-ui-common"))
            implementation(project(":mockzilla-common"))
            implementation(project(":mockzilla-management"))
            implementation(project(":mockzilla-codegen"))

            /* ADB */
            implementation(libs.adam)

            /* Serialization */
            implementation(libs.kotlinx.serialization.json)

            /* Logging */
            implementation(libs.kermit)

            /* Version Handling */
            implementation(libs.semver)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            /* ViewModel */
            implementation(libs.androidx.lifecycleViewModelCompose)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)

            implementation(libs.androidx.compose.activity)
            implementation(libs.ui.tooling.preview)

            /* Mockzilla */
            // Android target is only used for development since it's a better dev experience than desktop
            // So using mockzilla to have a "Mock app" to connect to
            implementation(project(":mockzilla"))
            implementation(libs.ktor.client.core)
        }
        val androidHostTest by getting {
            dependencies {
                implementation(libs.androidx.test.junit)
                implementation(libs.testParamInjector)
            }
        }
        val desktopMain by getting {
            dependencies {
                /* Compose */
                implementation(compose.desktop.currentOs) {
                    exclude("org.jetbrains.compose.material")
                }

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.swing)

                /* Zeroconf */
                implementation(libs.jmdns.jmdns)
            }
        }
        val desktopTest by getting {
            dependencies {
                implementation(libs.turbine)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.mockk)
            }
        }
    }
    compilerOptions {
        freeCompilerArgs.addAll(CompilerConfig.freeCompilerArgs)
        freeCompilerArgs.add("-opt-in=com.apadmi.mockzilla.lib.InternalMockzillaApi")
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs("-Dapple.awt.application.appearance=system", "-Djava.net.preferIPv4Stack=true")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = artifactName
        }
    }
}

buildkonfig {
    packageName = "$group.mockzilla.desktop"
    exposeObjectWithName = "MockzillaDesktopBuildConfig"

    defaultConfigs {
        buildConfigField(
            STRING,
            "version",
            version.toString() + ("-SNAPSHOT".takeIf { isSnapshot() } ?: "")
        )
        buildConfigField(BOOLEAN, "isSnapshot", isSnapshot().toString())
    }
}

dependencies {
    // Use the configurations created by the Conveyor plugin to tell Gradle/Conveyor where to find the artifacts for each platform.
    linuxAmd64(libs.desktop.jvm.linux.x64)
    macAmd64(libs.desktop.jvm.macos.x64)
    macAarch64(libs.desktop.jvm.macos.arm64)
    windowsAmd64(libs.desktop.jvm.windows.x64)
}

aboutLibraries {
    export {
        outputFile = file("src/commonMain/composeResources/files/aboutlibraries.json")
    }

    collect {
        configPath = file("licenses-config")

        // GitHub token to raise API request limit to allow fetching more licenses.
        // Needed for fetching licenses at build time.
        gitHubApiToken = githubToken()

        // Enable fetching of "remote" licenses.  Uses the API of supported source hosts
        // See https://github.com/mikepenz/AboutLibraries#special-repository-support
        fetchRemoteLicense = true

        // Enables fetching of "remote" funding information. Uses the API of supported source hosts
        // See https://github.com/mikepenz/AboutLibraries#special-repository-support
        fetchRemoteFunding = true

        // Enable inclusion of `platform` dependencies in the library report
        includePlatform = true
    }
}
