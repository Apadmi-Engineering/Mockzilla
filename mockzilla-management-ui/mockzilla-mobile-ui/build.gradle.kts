import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.MobileUiConfig
import com.apadmi.mockzilla.configureCommonProperties
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.isSigningEnabled
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.vanniktechPublish)
    kotlin("native.cocoapods") apply true
}

val artifactName = "mockzilla-mobile-ui"

kotlin {
    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "0.0.3" // x-release-please-version

    androidTarget()
    jvmToolchain(JavaConfig.toolchain)

    cocoapods {
        name = "SwiftMockzillaMobileUi"
        summary = "Embedded UI for configuring and controlling the Mockzilla server from within an app"
        homepage = "https://apadmi-engineering.github.io/Mockzilla/"
        framework {
            baseName = artifactName
        }
        license = "{:type => 'MIT', :file => 'LICENSE'}"
        // This is explicitly `getVersion()` and not `version`! The latter is shadowed in `cocoapods` scope.
        source = "{ :git => 'https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi.git', :tag => 'v${project.version}' }"
        extraSpecAttributes["vendored_frameworks"] = "'mockzilla_mobile_ui.xcframework'"
        extraSpecAttributes["source_files"] = "'Sources/SwiftMockzillaMobileUi/SwiftMockzillaMobileUi.swift'"
        extraSpecAttributes["swift_version"] = "'5.9.2'"

        ios.deploymentTarget = "13.0"
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

    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        commonMain.dependencies {
            /* Compose */
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.navigation.compose)

            /* Localisable Strings */
            implementation(libs.lyricist.library)

            /* DI */
            implementation(libs.koin.core)

            /* Mockzilla Management */
            implementation(project(":mockzilla-management-ui:mockzilla-management-ui-common"))
            implementation("com.apadmi:mockzilla-common:${MobileUiConfig.coreVersionForManagementUi}")

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
            implementation(compose.preview)
            implementation(compose.components.uiToolingPreview)
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.androidx.test.junit)
                implementation(libs.testParamInjector)
            }
        }
    }
}

android {
    namespace = "$group.mockzilla.mobile.ui"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk

        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (isSigningEnabled()) {
        signAllPublications()
    }

    coordinates(group.toString(), artifactName, version.toString())

    pom {
        name.set("Mockzilla Mobile UI")
        description.set("User Interface for manipulating Mockzilla endpoint configs at runtime")

        configureCommonProperties()
    }
}
