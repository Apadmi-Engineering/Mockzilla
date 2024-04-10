import com.apadmi.mockzilla.JavaConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm {
        jvmToolchain(JavaConfig.toolchain)
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
