package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.connection.IpAddress
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.StatefulDevice

fun Device.Companion.dummy() = Device("ip", "port")

fun StatefulDevice.Companion.dummy() = StatefulDevice(
    device = Device.dummy(),
    metaData = MetaData.dummy(),
    isConnected = false,
    isCompatibleMockzillaVersion = true
)

fun AdbConnection.Companion.dummy(ipAddresses: List<String> = emptyList()) = AdbConnection(
    deviceSerial = "serial",
    isActive = true,
    ipAddresses = ipAddresses.map { IpAddress(it) }
)
internal fun DeviceTabsViewModel.State.DeviceTabEntry.Companion.dummy() =
    DeviceTabsViewModel.State.DeviceTabEntry(
        appName = "",
        deviceName = "",
        isActive = true,
        isConnected = true,
        underlyingDevice = Device.dummy()
    )
