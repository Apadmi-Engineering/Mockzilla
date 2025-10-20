package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.connection.IpAddress
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.StatefulDevice

fun Device.Companion.dummy() = Device("ip", "port")
fun DeviceTabsViewModel.State.DeviceTabEntry.Companion.dummy() =
    DeviceTabsViewModel.State.DeviceTabEntry(
        name = "",
        isActive = true,
        isConnected = true,
        underlyingDevice = Device.dummy()
    )

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
