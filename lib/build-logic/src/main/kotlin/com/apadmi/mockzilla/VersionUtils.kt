package com.apadmi.mockzilla

import org.gradle.api.Project
import java.io.File

val Project.debugVersionFile get() = File("${project.rootProject.projectDir.parent}/debug-version.txt")
val Project.versionFile get() = File("${project.rootProject.projectDir.parent}/version.txt")

fun Project.extractVersion(): String {
    return debugVersionFile
        .takeIf { it.exists() }
        ?.readText()
        ?.trim()?.takeUnless { it.isBlank() } ?: versionFile.readText().trim()
}
