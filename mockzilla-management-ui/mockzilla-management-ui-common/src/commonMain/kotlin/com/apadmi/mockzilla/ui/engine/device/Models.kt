@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaConnectionConfig

data class Device(override val ip: String, override val port: String) : MockzillaConnectionConfig {
    companion object
}

/**
 * @property device
 * @property metaData
 * @property isConnected
 * @property isCompatibleMockzillaVersion
 */
data class StatefulDevice(
    val device: Device,
    val metaData: MetaData,
    val isConnected: Boolean,
    val isCompatibleMockzillaVersion: Boolean
) {
    companion object
}
