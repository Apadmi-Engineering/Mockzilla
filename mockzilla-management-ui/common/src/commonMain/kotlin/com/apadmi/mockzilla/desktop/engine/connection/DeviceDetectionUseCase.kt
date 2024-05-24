package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.engine.connection.jmdns.ServiceInfoWrapper
import com.apadmi.mockzilla.lib.models.MetaData.Companion.parseMetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


interface DeviceDetectionUseCase {
    suspend fun prepareForConnection(device: DetectedDevice): Result<String>

    val onChangeEvent: Flow<Unit>
    val devices: List<DetectedDevice>
}

class DeviceDetectionUseCaseImpl(
    private val localIpAddress: () -> String,
    private val adbConnectorUseCase: AdbConnectorUseCase
) : DeviceDetectionUseCase {
    private val deviceCache = mutableMapOf<String, DetectedDevice>()
    override suspend fun prepareForConnection(device: DetectedDevice): Result<String> {
        return when (device.metaData?.runTarget) {
            RunTarget.AndroidEmulator -> {
                val adbConnection = device.adbConnection ?: findAdbConnection(device.hostAddresses)
                ?: return Result.failure(Exception("Failed to detect emulator with adbo"))
                adbConnectorUseCase.setupPortForwardingIfNeeded(
                    adbConnection,
                    0,
                    device.port
                ).map { "127.0.0.1:${it.localPort}" }
            }

            RunTarget.iOSSimulator -> Result.success("127.0.0.1:${device.port}")
            RunTarget.iOSDevice,
            RunTarget.AndroidDevice,
            RunTarget.Jvm,
            null -> Result.success("${device.hostAddress}:${device.port}")
        }
    }

    override val onChangeEvent = MutableSharedFlow<Unit>(replay = 1)

    private val mutex = Mutex()
    internal suspend fun onChangedServiceEvent(info: ServiceInfoWrapper) = mutex.withLock {
        val adbConnection = findAdbConnection(info.hostAddresses)
        val metaData = runCatching { info.attributes.parseMetaData() }.getOrNull()

        val state = when (info.state) {
            ServiceInfoWrapper.State.Found -> DetectedDevice.State.Resolving
            ServiceInfoWrapper.State.Resolved -> when (metaData?.runTarget) {
                RunTarget.AndroidEmulator -> if (adbConnection != null) {
                    DetectedDevice.State.ReadyToConnect
                } else {
                    DetectedDevice.State.NotYourSimulator
                }

                RunTarget.iOSSimulator -> if (info.hostAddresses.contains(localIpAddress())) {
                    DetectedDevice.State.ReadyToConnect
                } else {
                    DetectedDevice.State.NotYourSimulator
                }

                else -> DetectedDevice.State.ReadyToConnect
            }

            ServiceInfoWrapper.State.Removed -> DetectedDevice.State.Removed
        }

        val existingDevice = deviceCache[info.connectionName]
        val device = when {
            state == DetectedDevice.State.Removed -> existingDevice?.copy(
                state = DetectedDevice.State.Removed
            )
            // For some reason sometimes the "Resolving" callback comes in after the "Ready to connect"
            // callback so ignore this event
            existingDevice?.state == DetectedDevice.State.ReadyToConnect
                    && state == DetectedDevice.State.Resolving -> existingDevice

            else -> {
                null
            }
        }

        deviceCache[info.connectionName] = (device ?: DetectedDevice(
            info.connectionName,
            metaData,
            info.hostAddress,
            info.hostAddresses,
            info.port,
            adbConnection,
            state
        ))
        onChangeEvent.emit(Unit)
    }

    private suspend fun findAdbConnection(hostAddresses: List<String>) =
        adbConnectorUseCase.listConnectedDevices().getOrNull()?.firstOrNull {
            it.ipAddresses.intersect(hostAddresses.toSet()).isNotEmpty()
        }

    override val devices: List<DetectedDevice>
        get() = deviceCache.values.toList()

}

