package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.management.MockzillaManagement

data class Device(val deviceName: String, val ip: String, val port: String) {
    val connection = MockzillaManagement.ConnectionConfig(ip, port)
}