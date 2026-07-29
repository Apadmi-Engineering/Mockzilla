import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.CompilerConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.MobileUiConfig
import com.apadmi.mockzilla.configureCommonProperties
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.isMobileUiDeployBuild
import com.apadmi.mockzilla.isSigningEnabled
import com.apadmi.mockzilla.isSnapshot
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
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
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.dokka) apply true
    kotlin("native.cocoapods") apply true
}

val artifactName = "mockzilla-mobile-ui"

// Kotlin changes `-` for `_` in framework names which breaks appstore uploads
val xcFrameworkName = "mockzillamobileui"

kotlin {
    explicitApi()

    // Managed automatically by release-please PRs.
    version = project.injectedVersion() ?: "1.1.0" // x-release-please-version

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

        // The vendored xcframework only ships arm64 slices (device + simulator),
        // so exclude x86_64 on the simulator to avoid "no matching slice" /
        // "unable to resolve module 'mockzillamobileui'" build failures.
        extraSpecAttributes["pod_target_xcconfig"]  = "{ 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'x86_64' }"
        extraSpecAttributes["user_target_xcconfig"] = "{ 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'x86_64' }"

        ios.deploymentTarget = "13.0"
    }

    val xcf = XCFramework()
    listOf(
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
            implementation(libs.runtime)
            implementation(libs.material3)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.navigation.compose)


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
        freeCompilerArgs.add("-opt-in=com.apadmi.mockzilla.lib.InternalMockzillaApi")
    }
}

buildkonfig {
    packageName = "$group.mockzilla.mobile.ui"
    exposeObjectWithName = "MockzillaMobileUiBuildConfig"

    defaultConfigs {
        buildConfigField(
            STRING,
            "version",
            version.toString()
        )
        buildConfigField(BOOLEAN, "isSnapshot", isSnapshot().toString())
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

    // Docs are hosted on the website; an empty javadoc jar satisfies Maven Central's
    // validation without shipping the full Dokka render in every artifact
    configure(KotlinMultiplatform(javadocJar = JavadocJar.Empty(), sourcesJar = true))

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
