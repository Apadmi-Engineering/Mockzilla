import com.apadmi.mockzilla.JavaConfig
import com.apadmi.mockzilla.AndroidConfig

plugins {
    alias(libs.plugins.compose)
    kotlin("android")
    alias(libs.plugins.android.app)
}

android {
    namespace = "$group.mockzilla.kmm.sample"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk

        versionCode = 1
        versionName = rootProject.version.toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
}

kotlin {
    jvmToolchain(JavaConfig.toolchain)
}

dependencies {
    implementation(project(":samples:demo-kmm:shared"))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.uiToolingPreview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.activity)
}