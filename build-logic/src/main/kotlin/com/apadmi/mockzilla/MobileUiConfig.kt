package com.apadmi.mockzilla

import org.gradle.api.Project

object MobileUiConfig {
    const val coreVersionForManagementUi = "3.0.0-alpha1"
}

fun Project.isMobileUiDeployBuild() = properties["is_building_for_deployment"].toString().toBoolean()
