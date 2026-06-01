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
            RunTarget.Js,
            null -> Result.success("${device.hostAddress}:${device.port}")
        }.map { IpAddress(it) }
    }

    internal suspend fun onChangedServiceEvent(event: DeviceDiscoveryEvent) = mutex.withLock {
        val metaData = event.metaData ?: runCatching { event.attributes.parseMetaData() }.getOrNull()

        event.adbConnection?.let {
            handleAdbEvent(event, metaData)
        } ?: handleZeroConfEvent(event, metaData)
    }

    private suspend fun handleAdbEvent(event: DeviceDiscoveryEvent, metaData: MetaData?) {
        val cacheKey = event.connectionName

        if (event.state == DeviceDiscoveryEvent.State.Removed) {
            deviceCache[cacheKey]?.let {
                deviceCache[cacheKey] = it.copy(state = DetectedDevice.State.Removed)
                onChangeEvent.emit(Unit)
            }
            return
        }

        // Skip if mDNS already found this specific port on this device — prefer the mDNS entry
        val alreadyFoundByMdns = deviceCache.values.any {
            it.adbConnection?.deviceSerial == event.adbConnection?.deviceSerial &&
                    it.port == event.port &&
                    it.connectionName != cacheKey
        }
        if (alreadyFoundByMdns) {
            return
        }

        val device = DetectedDevice(
            connectionName = cacheKey,
            metaData = metaData,
            hostAddress = "127.0.0.1",
            hostAddresses = listOf(IpAddress("127.0.0.1")),
            port = event.port,
            adbConnection = event.adbConnection,
            state = DetectedDevice.State.ReadyToConnect
        )
        if (deviceCache[cacheKey] != device) {
            deviceCache[cacheKey] = device
            onChangeEvent.emit(Unit)
        }
    }

    private suspend fun handleZeroConfEvent(event: DeviceDiscoveryEvent, metaData: MetaData?) {
        val existingDevice = deviceCache[event.connectionName]
        val adbConnection = if (metaData?.runTarget == RunTarget.AndroidEmulator) {
            existingDevice?.adbConnection
                ?: findAdbConnection(event.hostAddresses.map { IpAddress(it) })
        } else {
            null
        }

        val state = determineNewDeviceState(event, metaData, adbConnection)

        val device = existingDevice?.updateDevice(state, event) ?: DetectedDevice(
            event.connectionName,
            metaData,
            event.hostAddress,
            event.hostAddresses.map { IpAddress(it) },
            event.port,
            adbConnection,
            state
        )

        // mDNS found the same emulator+port that ADB already registered — drop the ADB entry so
        // the device doesn't appear twice.
        val adbKey = "adb:${adbConnection?.deviceSerial}:${event.port}"
        if (adbConnection != null && deviceCache.containsKey(adbKey)) {
            deviceCache.remove(adbKey)
        }
        deviceCache[event.connectionName] = device
        if (existingDevice != device) {
            onChangeEvent.emit(Unit)
        }
    }

    fun DetectedDevice?.updateDevice(newState: DetectedDevice.State, event: DeviceDiscoveryEvent) = when {
        this != null && newState == DetectedDevice.State.Removed -> copy(
            state = DetectedDevice.State.Removed
        )
        // For some reason sometimes the "Resolving" callback comes in after the "Ready to connect"
        // callback so ignore this event
        this != null && this.state in listOf(
            DetectedDevice.State.ReadyToConnect,
            DetectedDevice.State.NotYourSimulator
        ) && newState == DetectedDevice.State.Resolving -> this

        // jmDNS sometimes seems to emit "Found" for removed devices, so ignore these
        newState == DetectedDevice.State.Removed && event.state == DeviceDiscoveryEvent.State.Found -> this
        else -> null
    }

    private fun determineNewDeviceState(
        event: DeviceDiscoveryEvent,
        metaData: MetaData?,
        adbConnection: AdbConnection?
    ) = when {
        event.state == DeviceDiscoveryEvent.State.Removed -> DetectedDevice.State.Removed
        // If we have metadata it doesn't really matter if the underlying framework considers the
        // device resolved or not, we already have what we need
        metaData != null || event.state == DeviceDiscoveryEvent.State.Resolved -> when (metaData?.runTarget) {
            RunTarget.AndroidEmulator -> adbConnection?.let {
                DetectedDevice.State.ReadyToConnect
            } ?: DetectedDevice.State.NotYourSimulator

            RunTarget.IosSimulator -> if (event.hostAddresses.any(isLocalIpAddress)) {
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
