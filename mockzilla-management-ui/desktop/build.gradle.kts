import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.conveyor)
}

kotlin {
    jvmToolchain(JavaConfig.toolchain)
    jvm {
        withJava()
    }
    sourceSets {
        jvmMain.dependencies {
            implementation(project(":mockzilla-management-ui:common"))
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mockzilla-management-ui"
            packageVersion = rootProject.version.toString().split("-").first()
        }
    }
}

dependencies {
    // Use the configurations created by the Conveyor plugin to tell Gradle/Conveyor where to find the artifacts for each platform.
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

configurations.all {
    attributes {
        // Temporary fix for https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731 
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
