package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MetaData.Companion.parseMetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.connection.DetectedDevice
import com.apadmi.mockzilla.ui.engine.connection.IpAddress
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface DeviceDetectionUseCase {
    val onChangeEvent: Flow<Unit>
    val devices: List<DetectedDevice>
    suspend fun prepareForConnection(device: DetectedDevice): Result<IpAddress>
}

class DeviceDetectionUseCaseImpl(
    private val isLocalIpAddress: (String) -> Boolean,
    private val adbConnectorService: AdbConnectorService
) : DeviceDetectionUseCase {
    private val deviceCache = mutableMapOf<String, DetectedDevice>()
    override val onChangeEvent = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val mutex = Mutex()

    override val devices: List<DetectedDevice>
        get() = deviceCache.values.toList()

    override suspend fun prepareForConnection(device: DetectedDevice): Result<IpAddress> {
        return when (device.metaData?.runTarget) {
            RunTarget.AndroidEmulator -> {
                val adbConnection = device.adbConnection ?: findAdbConnection(device.hostAddresses)
                    ?: return Result.failure(Exception("Failed to detect emulator with adb"))
                adbConnectorService.setupPortForwardingIfNeeded(
                    adbConnection,
                    0,
                    device.port
                ).map { "127.0.0.1:${it.localPort}" }
            }

            RunTarget.IosSimulator -> Result.success("127.0.0.1:${device.port}")
            RunTarget.IosDevice,
            RunTarget.AndroidDevice,
            RunTarget.Jvm,
            null -> Result.success("${device.hostAddress}:${device.port}")
        }.map { IpAddress(it) }
    }

    internal suspend fun onChangedServiceEvent(info: ServiceInfoWrapper) = mutex.withLock {
        val metaData = runCatching { info.attributes.parseMetaData() }.getOrNull()
        val existingDevice = deviceCache[info.connectionName]
        val adbConnection = if (metaData?.runTarget == RunTarget.AndroidEmulator) {
            existingDevice?.adbConnection
                ?: findAdbConnection(info.hostAddresses.map { IpAddress(it) })
        } else {
            null
        }

        val state = determineNewDeviceState(info, metaData, adbConnection)

        val device = when {
            existingDevice != null && state == DetectedDevice.State.Removed -> existingDevice.copy(
                state = DetectedDevice.State.Removed
            )
            // For some reason sometimes the "Resolving" callback comes in after the "Ready to connect"
            // callback so ignore this event
            existingDevice != null && existingDevice.state in listOf(
                DetectedDevice.State.ReadyToConnect,
                DetectedDevice.State.NotYourSimulator
            ) && state == DetectedDevice.State.Resolving -> existingDevice

            // jmDNS sometimes seems to emit "Found" for removed devices, so ignore these
            existingDevice?.state == DetectedDevice.State.Removed && info.state == ServiceInfoWrapper.State.Found -> existingDevice
            else -> null
        } ?: DetectedDevice(
            info.connectionName,
            metaData,
            info.hostAddress,
            info.hostAddresses.map { IpAddress(it) },
            info.port,
            adbConnection,
            state
        )

        deviceCache[info.connectionName] = device
        if (existingDevice != device) {
            onChangeEvent.emit(Unit)
        }
    }

    private fun determineNewDeviceState(
        info: ServiceInfoWrapper,
        metaData: MetaData?,
        adbConnection: AdbConnection?
    ) = when {
        info.state == ServiceInfoWrapper.State.Removed -> DetectedDevice.State.Removed
        // If we have metadata it doesn't really matter if the underlying framework considers the
        // device resolved or not, we already have what we need
        metaData != null || info.state == ServiceInfoWrapper.State.Resolved -> when (metaData?.runTarget) {
            RunTarget.AndroidEmulator -> adbConnection?.let {
                DetectedDevice.State.ReadyToConnect
            } ?: DetectedDevice.State.NotYourSimulator

            RunTarget.IosSimulator -> if (info.hostAddresses.any(isLocalIpAddress)) {
                DetectedDevice.State.ReadyToConnect
            } else {
                DetectedDevice.State.NotYourSimulator
            }

            else -> DetectedDevice.State.ReadyToConnect
        }
        else -> DetectedDevice.State.Resolving
    }

    private suspend fun findAdbConnection(
        hostAddresses: List<IpAddress>
    ) = matchAdbDeviceFromHostAddresses(
        adbConnectorService.listConnectedDevices().getOrNull() ?: emptyList(),
        hostAddresses.toSet()
    )

    companion object {
        fun matchAdbDeviceFromHostAddresses(
            connections: List<AdbConnection>,
            hostAddresses: Set<IpAddress>
        ) = connections.firstOrNull {
            it.ipAddresses.intersect(hostAddresses).isNotEmpty()
        }
    }
}
