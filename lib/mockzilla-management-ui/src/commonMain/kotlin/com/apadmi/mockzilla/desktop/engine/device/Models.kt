@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.management.MockzillaManagement.*

data class Device(override val ip: String, override val port: String) : ConnectionConfig
/**
 * @property device
 * @property name
 * @property isConnected
 * @property connectedAppPackage
 */
data class StatefulDevice(
    val device: Device,
    val name: String,
    val isConnected: Boolean,
    val connectedAppPackage: String
)
