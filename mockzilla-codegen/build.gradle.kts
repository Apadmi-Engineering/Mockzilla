import com.apadmi.mockzilla.JavaConfig
import org.gradle.kotlin.dsl.repositories

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(JavaConfig.toolchain)
    jvm {
        testRuns["test"].executionTask.configure { useJUnitPlatform() }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.swagger.parser)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}