import com.apadmi.mockzilla.AndroidConfig

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    implementation(project(":mockzilla-management-ui:common"))
    implementation(libs.androidx.compose.activity)
}

android {
    compileSdk = AndroidConfig.targetSdk
    namespace = group.toString()
    defaultConfig {
        applicationId = group.toString()
        minSdk = AndroidConfig.minSdk
        versionCode = System.getProperties().getOrDefault("versionCode", "1").toString().toInt()
        versionName = versionName
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.asProvider().get()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
