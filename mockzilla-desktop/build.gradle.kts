import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.spotless) apply true
}

group = "com.apadmi.mockzilla.desktop"
version = "0.0.0"

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                /* Compose */
                implementation(compose.desktop.currentOs)
                implementation(libs.material.icons)
                implementation(libs.material3)

                /* Coroutines */
                implementation(libs.kotlinx.coroutines.swing)

                /* DI */
                implementation(libs.koin.core)

            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mockzilla-desktop"
            packageVersion = "1.0.0"
        }
    }
}


configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    kotlin {
        target("src/**/*.kt")

        // DO NOT ADD **/ REGEXES HERE FOR EXCLUSIONS (OR TARGETS) WITHOUT EXTENSIVE TESTING - THEY
        // CAN EASILY EXPLODE SPOTLESS RUNTIMES FROM SECONDS TO MINUTES
        targetExclude()

        diktat("1.2.5").configFile("diktat-analysis.yml")

        // TODO: Add this back in.
//        // Checks to make sure some basic components are using material3 instead of material2
//        custom("Material2 police") { str ->
//            UseMaterial3Task.execute(str)
//        }

        // Bump if tweaking the custom step (required to retain performance: https://javadoc.io/doc/com.diffplug.spotless/spotless-plugin-gradle/latest/com/diffplug/gradle/spotless/FormatExtension.html#bumpThisNumberIfACustomStepChanges-int-)
        bumpThisNumberIfACustomStepChanges(15)
    }
}

