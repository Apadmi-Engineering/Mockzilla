package com.apadmi.mockzilla

import org.gradle.api.Project

fun Project.injectedVersion() = properties["version"].toString().takeUnless { it.isBlank() || it == "unspecified" }