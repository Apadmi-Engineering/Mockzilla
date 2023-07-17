plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.apadmi.mockzilla.demo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.apadmi.mockzilla.demo"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    /* Android */
    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.ui:ui-tooling:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.0")
    implementation("androidx.compose.foundation:foundation:1.3.0")
    implementation("androidx.compose.material:material:1.3.0")
    implementation("androidx.activity:activity-compose:1.6.1")

    /* Mockzilla */
    implementation("com.apadmi.mockzilla:mockzilla:${extractMockzillaVersion()}")

    /* Json parsing */
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")

    /* Networking */
    implementation("io.ktor:ktor-client-core:2.1.3")
    implementation("io.ktor:ktor-client-cio:2.1.3")

    /* Testing */
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.1")
}

fun extractMockzillaVersion(): String {
    val version = providers.fileContents(
        rootProject.layout.projectDirectory.file("version")
    ).asText.get()

    val debugVersionFile = rootProject.layout.projectDirectory.file("debug-version")
    val debugVersion = debugVersionFile.takeIf { it.asFile.exists() }?.let {
        providers.fileContents(it).asText.orNull
    }?.takeUnless { it.isBlank() }

    return debugVersion ?: version
}