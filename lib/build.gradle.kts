plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("multiplatform").version("1.8.22").apply(false)
    kotlin("plugin.serialization").version("1.8.20")
    id("com.diffplug.spotless").version("6.11.0")
    id("com.google.devtools.ksp").version("1.8.21-1.0.11")
    id("org.jetbrains.dokka").version("1.8.20")
}

buildscript {
    dependencies {
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")
        classpath(":build-logic")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {

    kotlin {
        target("**/*.kt")
        targetExclude(
            "build-logic/build/**", "build/**", "**/mockzilla/build/**", "fastlane/**",
            "fastlane-build/**", "**/FlutterMockzilla/**/*.g.kt"
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