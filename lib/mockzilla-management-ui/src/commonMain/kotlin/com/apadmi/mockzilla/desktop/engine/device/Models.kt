@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.management.MockzillaManagement

/**
 * @property deviceName
 * @property ip
 * @property port
 */
data class Device(
    val deviceName: String,
    val ip: String,
    val port: String
) {
    val connection = MockzillaManagement.ConnectionConfig(ip, port)
}
