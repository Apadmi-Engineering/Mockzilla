import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.CompilerConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.configureCommonProperties
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.isDevelopmentBuild
import com.apadmi.mockzilla.isSigningEnabled
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.vanniktechPublish)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.dokka) apply true
}

val artifactName = "mockzilla-common"

kotlin {
    explicitApi()

    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "4.0.0" // x-release-please-version
    androidTarget {
        publishLibraryVariants()
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

    jvm()
    jvmToolchain(JavaConfig.toolchain)

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        commonMain.dependencies {
            /* Kotlin */
            implementation(libs.kotlinx.coroutines.core)

            /* Ktor */
            implementation(libs.ktor.server.core)

            /* Serialization */
            implementation(libs.kotlinx.serialization.json)

            /* Date Time */
            implementation(libs.kotlinx.datetime)

            /* Logging */
            implementation(libs.kermit)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))

            implementation(libs.kotlinx.coroutines.test)
        }
    }
    compilerOptions {
        freeCompilerArgs.addAll(CompilerConfig.freeCompilerArgs)
        freeCompilerArgs.add("-opt-in=com.apadmi.mockzilla.lib.InternalMockzillaApi")
    }
}

android {
    namespace = "$group.mockzilla.common"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        testOptions.targetSdk = AndroidConfig.targetSdk

        consumerProguardFiles("mockzilla-proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}

buildkonfig {
    packageName = "$group.mockzilla.lib"
    exposeObjectWithName = "MockzillaBuildConfig"

    defaultConfigs {
        buildConfigField(BOOLEAN, "isDevelopmentBuild", isDevelopmentBuild().toString())
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
        name.set(artifactName)
        description.set(
            """
            A utility module containing common utilities and models used by multiple different mockzilla libraries.
        """.trimIndent()
        )

        configureCommonProperties()
    }
}