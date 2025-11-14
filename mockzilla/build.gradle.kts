import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.CompilerConfig
import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.injectedVersion
import com.apadmi.mockzilla.configureCommonProperties
import com.apadmi.mockzilla.isSigningEnabled
import com.apadmi.mockzilla.karmaDirName
import com.apadmi.mockzilla.prepareKarmaFile
import com.apadmi.mockzilla.serviceWorkerFileName
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.vanniktechPublish)
    kotlin("native.cocoapods") apply true
}

val artifactName = "mockzilla"

kotlin {
    androidTarget {
        publishLibraryVariants()
    }

    // Managed automatically by release-please PRs
    version = project.injectedVersion() ?: "3.0.0-alpha1" // x-release-please-version

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

    // Enables KDocs comments export to Objective-C headers.
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions.freeCompilerArgs.add("-Xexport-kdoc")
        }
    }

    jvm()
    jvmToolchain(JavaConfig.toolchain)

    js {
        browser()
        binaries.executable()
    }

    cocoapods {
        name = "SwiftMockzilla"
        summary = "A solution for running and configuring a local HTTP server to mimic REST API endpoints used by your application."
        homepage = "https://mockzilla.apadmi.dev/"
        framework {
            baseName = artifactName
        }
        license = "{:type => 'MIT', :file => 'LICENSE'}"
        // This is explicitly `getVersion()` and not `version`! The latter is shadowed in `cocoapods` scope.
        source = "{ :git => 'https://github.com/Apadmi-Engineering/SwiftMockzilla.git', :tag => 'v${project.version}' }"
        extraSpecAttributes["vendored_frameworks"] = "'Mockzilla.xcframework'"
        extraSpecAttributes["source_files"] = "'Sources/SwiftMockzilla/SwiftMockzilla.swift'"
        extraSpecAttributes["swift_version"] = "'5.9.2'"

        ios.deploymentTarget = "13.0"
    }

    applyDefaultHierarchyTemplate()

    sourceSets {

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        val allButWebMain by creating {
            dependsOn(commonMain.get())
        }
        androidMain.get().dependsOn(allButWebMain)
        iosMain.get().dependsOn(allButWebMain)
        jvmMain.get().dependsOn(allButWebMain)

        allButWebMain.dependencies {
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.server.rate.limit)
        }

        androidMain.dependencies {
            implementation(libs.play.services.ads.identifier)
        }

        webMain.dependencies {
            implementation(libs.kotlinx.browser)
            implementation(npm("msw", "2.11.6"))
        }

        commonMain.dependencies {
            /* Kotlin */
            implementation(libs.kotlinx.coroutines.core)
            api(project(":mockzilla-common"))

            /* Ktor */
            api(libs.ktor.server.core)

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
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
        }
        jsTest.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
    compilerOptions {
        freeCompilerArgs.addAll(CompilerConfig.freeCompilerArgs)
    }

    js {
        browser {
            testTask {
                doFirst { prepareKarmaFile() }
                useKarma {
                    useConfigDirectory(File(projectDir, karmaDirName))
                    useChromeHeadless()
                }
            }
            binaries.executable()
        }
    }
}

val copyServiceWorker = tasks.register<Copy>("copyServiceWorker") {
    from(rootProject.rootDir.resolve("js-scripts/")) {
        include(serviceWorkerFileName)
    }
    into(layout.projectDirectory.dir("src/jsTest/resources/"))
    description = "Copies the service worker file for JS testing."
}

tasks.getByPath(":mockzilla:jsTestProcessResources").dependsOn(copyServiceWorker)

android {
    namespace = group.toString()
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        testOptions.targetSdk = AndroidConfig.targetSdk

        consumerProguardFiles("mockzilla-proguard-rules.pro")
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")


    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}

buildkonfig {
    packageName = "$group.$artifactName"

    defaultConfigs {
        buildConfigField(STRING, "VERSION_NAME", version.toString())
    }
}

private val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (isSigningEnabled()) {
        logger.info("Signing key found  - signing")
        signAllPublications()
    } else {
        logger.info("No signing key found  - skipping signining")
    }

    coordinates(group.toString(), artifactName, version.toString())

    pom {
        name.set("Mockzilla")
        description.set("Solution for running and configuring a local HTTP server on mobile.")

        configureCommonProperties()
    }
}
