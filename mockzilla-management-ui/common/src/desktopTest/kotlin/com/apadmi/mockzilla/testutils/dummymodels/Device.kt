package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.desktop.engine.connection.AdbConnection
import com.apadmi.mockzilla.desktop.engine.connection.IpAddress
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel

fun Device.Companion.dummy() = Device("ip", "port")
fun DeviceTabsViewModel.State.DeviceTabEntry.Companion.dummy() =
    DeviceTabsViewModel.State.DeviceTabEntry(
        name = "",
        isActive = true,
        isConnected = true,
        underlyingDevice = Device.dummy()
    )

fun StatefulDevice.Companion.dummy() = StatefulDevice(Device.dummy(), "name", false, "package")

fun AdbConnection.Companion.dummy(ipAddresses: List<String> = emptyList()) = AdbConnection(
    deviceSerial = "serial",
    isActive = true,
    ipAddresses = ipAddresses.map { IpAddress(it) }
)
