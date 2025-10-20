package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.connection.IpAddress
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.StatefulDevice

fun Device.Companion.dummy() = Device("ip", "port")

fun StatefulDevice.Companion.dummy() = StatefulDevice(
    Device.dummy(),
    "name",
    false,
    "package",
    true
)

fun AdbConnection.Companion.dummy(ipAddresses: List<String> = emptyList()) = AdbConnection(
    deviceSerial = "serial",
    isActive = true,
    ipAddresses = ipAddresses.map { IpAddress(it) }
)
