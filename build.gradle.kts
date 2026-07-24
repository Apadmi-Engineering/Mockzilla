
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.spotless) apply true
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dokka) apply true
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.buildKonfig) apply false
    alias(libs.plugins.vanniktechPublish) apply false
    alias(libs.plugins.aboutlibraries) apply false
}

buildscript {
    dependencies {
        classpath(":build-logic")
    }
}

allprojects {
    group = "com.apadmi"

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencies {
    dokka(project(":mockzilla:"))
    dokka(project(":mockzilla-common:"))
    dokka(project(":mockzilla-management:"))
    dokka(project(":mockzilla-management-ui:mockzilla-desktop"))
    dokka(project(":mockzilla-management-ui:mockzilla-management-ui-common"))
    dokka(project(":mockzilla-management-ui:mockzilla-mobile-ui"))
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(File(System.getProperty("docsOutputDirectory", "temp")))
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    kotlin {
        target(
            "mockzilla/src/**/*.kt",
            "mockzilla-common/src/**/*.kt",
            "mockzilla-management/src/**/*.kt",
            "mockzilla-management-ui/**/*.kt",
            "samples/demo-android/src/**/*.kt",
            "samples/demo-kmm/shared/**/*.kt",
            "samples/demo-kmm/androidApp/**/*.kt"
        )
        targetExclude(
            "build-logic/build/**",
            "build/**",
            "mockzilla-management-ui/build/**",
            "mockzilla-management-ui/*/build/**",
            "*/build/**",
            "fastlane/**",
            "fastlane-build/**",
            "**/FlutterMockzilla/**/*.g.kt"
        )

        diktat("1.2.5").configFile("diktat-analysis.yml")

        // Bump if tweaking the custom step (required to retain performance: https://javadoc.io/doc/com.diffplug.spotless/spotless-plugin-gradle/latest/com/diffplug/gradle/spotless/FormatExtension.html#bumpThisNumberIfACustomStepChanges-int-)
        bumpThisNumberIfACustomStepChanges(14)
    }
}

project.afterEvaluate {
    tasks.getByPath(":mockzilla-common:preBuild").apply {
        dependsOn(
            tasks.register<Copy>("installGitHooks") {
                group = "git hooks"
                description = "Installs the local git hooks from the scripts directory."
                from(
                    File(rootProject.rootDir, "scripts/hooks/pre-commit")
                )
                into { File(rootProject.rootDir, ".git/hooks") }
                filePermissions {
                    user { execute = true }
                }

                // Always copy the file over because updates won't be copied over otherwise.
                outputs.upToDateWhen { false }
            }
        )
    }
}

tasks.withType<Test> {
    maxParallelForks = 1
}
