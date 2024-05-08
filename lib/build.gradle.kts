plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("8.4.0").apply(false)
    kotlin("multiplatform").version("1.9.24").apply(false)
    kotlin("plugin.serialization").version("1.9.24")
    id("com.diffplug.spotless").version("6.25.0")
    id("com.google.devtools.ksp").version("1.9.24-1.0.20")
    id("org.jetbrains.dokka").version("1.9.20")
}

buildscript {
    dependencies {
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.15.1")
        classpath(":build-logic")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    kotlin {
        target("**/*.kt")
        targetExclude("build-logic/build/**", "build/**", "*/build/**", "fastlane/**", "fastlane-build/**")

        diktat("1.2.5").configFile("diktat-analysis.yml")

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