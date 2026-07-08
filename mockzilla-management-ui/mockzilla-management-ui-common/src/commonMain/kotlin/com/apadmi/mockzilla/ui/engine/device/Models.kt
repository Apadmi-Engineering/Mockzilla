@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaConnectionConfig

@InternalMockzillaApi
public data class Device(override val ip: String, override val port: String) : MockzillaConnectionConfig {
    public companion object
}

@InternalMockzillaApi
public data class StatefulDevice(
    val device: Device,
    val metaData: MetaData,
    val isConnected: Boolean,
    val isCompatibleMockzillaVersion: Boolean
) {
    public companion object
}
