@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.management.MockzillaConnectionConfig

data class Device(override val ip: String, override val port: String) : MockzillaConnectionConfig {
    companion object
}

/**
 * @property device
 * @property name
 * @property isConnected
 * @property connectedAppPackage
 * @property isCompatibleMockzillaVersion
 */
data class StatefulDevice(
    val device: Device,
    val name: String,
    val isConnected: Boolean,
    val connectedAppPackage: String,
    val isCompatibleMockzillaVersion: Boolean
) {
    companion object
}
