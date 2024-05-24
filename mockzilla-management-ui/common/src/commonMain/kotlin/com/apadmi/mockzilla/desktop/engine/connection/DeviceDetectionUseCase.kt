package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.engine.connection.jmdns.ServiceInfoWrapper
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MetaData.Companion.parseMetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.skia.skottie.Logger

data class DetectedDevice(
    val connectionName: String,
    val metaData: MetaData?,
    val hostAddress: String,
    val hostAddresses: List<String>,
    val port: Int,
    val state: State
) {
    enum class State {
        Resolving,
        ReadyToConnect,
        NotYourSimulator,
        Removed
    }
}

interface DeviceDetectionUseCase {
    val onChangeEvent: Flow<Unit>
    val devices: List<DetectedDevice>
}

class DeviceDetectionUseCaseImpl(
    private val localIpAddress: () -> String,
    private val adbConnectorUseCase: AdbConnectorUseCase,
    private val scope: CoroutineScope = GlobalScope
) : DeviceDetectionUseCase {
    private val deviceCache = mutableMapOf<String, DetectedDevice>()
    override val onChangeEvent = MutableSharedFlow<Unit>(replay = 1)

    private val mutex = Mutex()
    internal suspend fun onChangedServiceEvent(info: ServiceInfoWrapper) = mutex.withLock {

        val adbConnection = adbConnectorUseCase.listConnectedDevices().getOrNull()?.firstOrNull {
            it.ipAddresses.intersect(info.hostAddresses.toSet()).isNotEmpty()
        }
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
            else -> { null }
        }

        deviceCache[info.connectionName] = (device ?: DetectedDevice(
            info.connectionName,
            metaData,
            info.hostAddress,
            info.hostAddresses,
            info.port,
            state
        ))
        onChangeEvent.emit(Unit)
    }

    override val devices: List<DetectedDevice>
        get() = deviceCache.values.toList()

}

