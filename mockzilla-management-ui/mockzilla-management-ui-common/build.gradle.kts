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
}

val artifactName = "mockzilla-management-ui-common"

kotlin {

    version = project.injectedVersion() ?: "0.0.3" // x-release-please-version

    androidTarget()
    jvmToolchain(JavaConfig.toolchain)
    jvm("desktop")

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
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.navigation.compose)

            /* Localisable Strings */
            implementation(libs.lyricist.library)

            /* DI */
            implementation(libs.koin.core)

            /* Coroutines */
            implementation(libs.kotlinx.coroutines.core)

            /* JSON */
            implementation(libs.kotlinx.serialization.json)

            /* Mockzilla Management */
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
        val androidUnitTest by getting {
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

                /* ADB */
                implementation(libs.adam)

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
    }
}

dependencies {
    /* Compose Previews */
    debugImplementation(compose.uiTooling)
}

android {
    namespace = "$group.mockzilla.mobile.ui.common"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        testOptions.targetSdk = AndroidConfig.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }

    buildFeatures {
        buildConfig = true
    }
}

configurations.all {
    attributes {
        // Temporary fix for https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (isSigningEnabled()) {
        signAllPublications()
    }

    coordinates(group.toString(), artifactName, version.toString())

    pom {
        name.set(artifactName)
        description.set(
            """
            A utility module containing common utilities and models used by Mockzilla desktop and embedded mobile ui.
        """.trimIndent()
        )

        configureCommonProperties()
    }
}