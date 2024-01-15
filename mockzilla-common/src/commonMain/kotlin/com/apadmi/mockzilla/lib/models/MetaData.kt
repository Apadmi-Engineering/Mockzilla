package com.apadmi.mockzilla.lib.models

import kotlinx.serialization.Serializable

/**
 * @property appName
 * @property appPackage
 * @property operatingSystemVersion
 * @property deviceModel
 * @property appVersion
 * @property operatingSystem
 * @property mockzillaVersion
 */
@Serializable
data class MetaData(
    val appName: String,
    val appPackage: String,
    val operatingSystemVersion: String,
    val deviceModel: String,
    val appVersion: String,
    val operatingSystem: String,
    val mockzillaVersion: String
)
