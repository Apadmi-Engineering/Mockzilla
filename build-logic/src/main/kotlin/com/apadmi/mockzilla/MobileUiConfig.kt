package com.apadmi.mockzilla

import org.gradle.api.Project

object MobileUiConfig {
    const val coreVersionForManagementUi = "2.4.1"
}

fun Project.isMobileUiDeployBuild() = properties["is_building_for_deployment"].toString().toBoolean()
