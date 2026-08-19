import com.apadmi.mockzilla.AndroidConfig
import com.apadmi.mockzilla.JavaConfig

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.app)
}

android {
    namespace = "$group.mockzilla.desktop.dev"
    compileSdk = AndroidConfig.targetSdk
    defaultConfig {
        applicationId = group.toString()
        minSdk = 26
        targetSdk = AndroidConfig.targetSdk
        versionCode = 1
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaConfig.version
        targetCompatibility = JavaConfig.version
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(project(":mockzilla-management-ui:mockzilla-desktop"))
    implementation(project(":mockzilla"))

    implementation(compose.runtime)
    implementation(libs.androidx.compose.activity)

    debugImplementation(libs.ui.tooling.preview)
}
