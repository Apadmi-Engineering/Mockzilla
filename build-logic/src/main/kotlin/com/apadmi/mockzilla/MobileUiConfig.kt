package com.apadmi.mockzilla

import org.gradle.api.Project

object MobileUiConfig {
    const val coreVersionForManagementUi = "UNKNOWN" // TODO: Update this when next version of mobile UI is ready
}

fun Project.isMobileUiDeployBuild() = properties["is_building_for_deployment"].toString().toBoolean()
