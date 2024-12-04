package com.apadmi.mockzilla

import org.gradle.api.Project

fun Project.injectedVersion() = if (project.hasProperty("version")) properties["version"].toString()
    .takeUnless { it.isBlank() || it == "unspecified" } else null