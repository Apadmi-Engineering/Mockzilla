import com.apadmi.mockzilla.extractVersion

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spotless) apply true
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dokka) apply true
}

buildscript {
    dependencies {
        classpath(libs.buildkonfig.gradle.plugin)
        classpath(":build-logic")
    }
}

allprojects {
    group = "com.apadmi"
    version = extractVersion()
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

tasks.dokkaHtmlMultiModule {
    outputDirectory.set(File(System.getProperty("docsOutputDirectory", "temp")))
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    kotlin {
        target("mockzilla/src/**/*.kt", "mockzilla-management/src/**/*.kt", "mockzilla-management-ui/src/**/*.kt")
        targetExclude(
            "build-logic/build/**",
            "build/**",
            "**/mockzilla/build/**",
            "fastlane/**",
            "fastlane-build/**"
        )

        diktat("1.2.1").configFile("diktat-analysis.yml")

        // Bump if tweaking the custom step (required to retain performance: https://javadoc.io/doc/com.diffplug.spotless/spotless-plugin-gradle/latest/com/diffplug/gradle/spotless/FormatExtension.html#bumpThisNumberIfACustomStepChanges-int-)
        bumpThisNumberIfACustomStepChanges(14)
    }
}

project.afterEvaluate {
    tasks.getByPath(":mockzilla:preBuild").apply {
        dependsOn(":spotlessApply")
    }
}

tasks.withType<Test> {
    maxParallelForks = 0
}