package com.apadmi.mockzilla

import org.gradle.api.Project

object MobileUiConfig {
    const val coreVersionForManagementUi = "to be updated during release"
}

fun Project.isMobileUiDeployBuild() = properties["is_building_for_deployment"].toString().toBoolean()
