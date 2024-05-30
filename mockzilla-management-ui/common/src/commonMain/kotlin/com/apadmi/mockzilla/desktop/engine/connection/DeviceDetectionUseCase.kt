package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MetaData.Companion.parseMetaData
import com.apadmi.mockzilla.lib.models.RunTarget

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
    private val localIpAddress: () -> String,
    private val adbConnectorService: AdbConnectorService
) : DeviceDetectionUseCase {
    private val deviceCache = mutableMapOf<String, DetectedDevice>()
    override val onChangeEvent = MutableSharedFlow<Unit>(replay = 1)
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
        val adbConnection = if (metaData?.isAndroid == true) {
            findAdbConnection(info.hostAddresses.map { IpAddress(it) })
        } else {
            null
        }
        val state = determineNewDeviceState(info, metaData, adbConnection)

        val existingDevice = deviceCache[info.connectionName]
        val device = when {
            state == DetectedDevice.State.Removed -> existingDevice?.copy(
                state = DetectedDevice.State.Removed
            )
            // For some reason sometimes the "Resolving" callback comes in after the "Ready to connect"
            // callback so ignore this event
            existingDevice?.state == DetectedDevice.State.ReadyToConnect && state == DetectedDevice.State.Resolving -> existingDevice
            else -> null
        }

        deviceCache[info.connectionName] = (device ?: DetectedDevice(
            info.connectionName,
            metaData,
            info.hostAddress,
            info.hostAddresses.map { IpAddress(it) },
            info.port,
            adbConnection,
            state
        ))
        onChangeEvent.emit(Unit)
    }

    private fun determineNewDeviceState(
        info: ServiceInfoWrapper,
        metaData: MetaData?,
        adbConnection: AdbConnection?
    ) = when (info.state) {
        ServiceInfoWrapper.State.Found -> DetectedDevice.State.Resolving
        ServiceInfoWrapper.State.Resolved -> when (metaData?.runTarget) {
            RunTarget.AndroidEmulator -> adbConnection?.let {
                DetectedDevice.State.ReadyToConnect
            } ?: DetectedDevice.State.NotYourSimulator

            RunTarget.IosSimulator -> if (info.hostAddresses.contains(localIpAddress())) {
                DetectedDevice.State.ReadyToConnect
            } else {
                DetectedDevice.State.NotYourSimulator
            }

            else -> DetectedDevice.State.ReadyToConnect
        }

        ServiceInfoWrapper.State.Removed -> DetectedDevice.State.Removed
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
