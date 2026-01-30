import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.CompilerConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.MobileUiConfig
import com.apadmi.mockzilla.configureCommonProperties
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.isMobileUiDeployBuild
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

// Kotlin changes `-` for `_` in framework names which breaks appstore uploads
val xcFrameworkName = "mockzillamobileui"

kotlin {
    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "1.0.0" // x-release-please-version

    androidTarget()
    jvmToolchain(JavaConfig.toolchain)

    cocoapods {
        name = "SwiftMockzillaMobileUi"
        summary = "Embedded UI for configuring and controlling the Mockzilla server from within an app"
        homepage = "https://mockzilla.apadmi.dev/"
        framework {
            baseName = xcFrameworkName
        }
        license = "{:type => 'MIT', :file => 'LICENSE'}"
        // This is explicitly `getVersion()` and not `version`! The latter is shadowed in `cocoapods` scope.
        source = "{ :git => 'https://github.com/Apadmi-Engineering/SwiftMockzillaMobileUi.git', :tag => 'v${project.version}' }"
        extraSpecAttributes["vendored_frameworks"] = "'${xcFrameworkName}.xcframework'"
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
            baseName = xcFrameworkName
            xcf.add(this)
        }
    }

    js {
        browser()
        binaries.executable()
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
            implementation(compose.components.uiToolingPreview)


            /* Localisable Strings */
            implementation(libs.lyricist.library)

            /* DI */
            implementation(libs.koin.core)

            /* Mockzilla Management */
            implementation(project(":mockzilla-management-ui:mockzilla-management-ui-common"))
            if (isMobileUiDeployBuild()) {
                //noinspection UseTomlInstead
                implementation("com.apadmi:mockzilla-common:${MobileUiConfig.coreVersionForManagementUi}")
                //noinspection UseTomlInstead
                implementation("com.apadmi:mockzilla-management:${MobileUiConfig.coreVersionForManagementUi}")
            } else {
                implementation(project(":mockzilla-common"))
                implementation(project(":mockzilla-management"))
            }

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
        jsMain.dependencies {
            implementation(compose.html.core)
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.androidx.test.junit)
                implementation(libs.testParamInjector)
            }
        }
    }
    compilerOptions {
        freeCompilerArgs.addAll(CompilerConfig.freeCompilerArgs)
    }
}

android {
    namespace = "$group.mockzilla.mobile.ui"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        testOptions.targetSdk = AndroidConfig.targetSdk

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
