package com.apadmi.mockzilla

import org.gradle.api.Project
import java.io.File

val Project.versionFile get() = File("${project.rootProject.projectDir}/version.txt")

fun Project.extractVersion() = versionFile.readText().trim()