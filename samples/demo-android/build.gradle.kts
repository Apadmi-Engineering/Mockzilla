import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "$group.mockzilla.android.sample"
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
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }

    buildFeatures {
        compose = true
    }
    
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    /* Android */
    implementation(compose.runtime)
    implementation(compose.material3)
    implementation(compose.preview)
    implementation(compose.components.resources)
    implementation(libs.androidx.compose.activity)

    /* Mockzilla */
    implementation(project(":mockzilla"))

    /* Json parsing */
    implementation(libs.kotlinx.serialization.json)

    /* Networking */
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.okhttp)
}
