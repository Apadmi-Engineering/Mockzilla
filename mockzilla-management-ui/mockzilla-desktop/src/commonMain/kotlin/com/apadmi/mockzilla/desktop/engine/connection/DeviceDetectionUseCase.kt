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
    suspend fun onAdbDiscoveredEmulator(
        connection: AdbConnection,
        metaData: MetaData,
        emulatorPort: Int
    )
    suspend fun onAdbEmulatorLost(deviceSerial: String, emulatorPort: Int)
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

        val device = existingDevice?.updateDevice(state, info) ?: DetectedDevice(
            info.connectionName,
            metaData,
            info.hostAddress,
            info.hostAddresses.map { IpAddress(it) },
            info.port,
            adbConnection,
            state
        )

        // mDNS found the same emulator+port that ADB already registered — drop the ADB entry so
        // the device doesn't appear twice.
        val adbKey = "adb:${adbConnection?.deviceSerial}:${info.port}"
        if (adbConnection != null && deviceCache.containsKey(adbKey)) {
            deviceCache.remove(adbKey)
        }
        deviceCache[info.connectionName] = device
        if (existingDevice != device) {
            onChangeEvent.emit(Unit)
        }
    }

    fun DetectedDevice?.updateDevice(newState: DetectedDevice.State, info: ServiceInfoWrapper) = when {
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
        newState == DetectedDevice.State.Removed && info.state == ServiceInfoWrapper.State.Found -> this
        else -> null
    }
    // "adb:$serial:$port" namespaces ADB-discovered entries so they don't collide with
    // mDNS-discovered entries (which use the Bonjour service name as the key). The port is
    // included because the same emulator may run multiple Mockzilla apps on different ports —
    // each (serial, port) pair is an independent discovered device.
    override suspend fun onAdbDiscoveredEmulator(
        connection: AdbConnection,
        metaData: MetaData,
        emulatorPort: Int
    ) = mutex.withLock {
        // "adb:$serial:$port" — port-qualified so two Mockzilla instances on the same emulator
        // each get their own cache entry and don't overwrite each other.
        val cacheKey = "adb:${connection.deviceSerial}:$emulatorPort"
        // Skip if mDNS already found this specific port on this device — prefer the mDNS entry
        // because it carries a real network address rather than a loopback forward.
        val alreadyFoundByMdns = deviceCache.values.any {
            it.adbConnection?.deviceSerial == connection.deviceSerial &&
                    it.port == emulatorPort &&
                    it.connectionName != cacheKey
        }
        if (alreadyFoundByMdns) {
            return@withLock
        }

        val device = DetectedDevice(
            connectionName = cacheKey,
            metaData = metaData,
            // ADB port forwarding binds on loopback — the real emulator address is
            // irrelevant here because traffic flows through the ADB tunnel.
            hostAddress = "127.0.0.1",
            hostAddresses = listOf(IpAddress("127.0.0.1")),
            port = emulatorPort,
            adbConnection = connection,
            state = DetectedDevice.State.ReadyToConnect
        )
        if (deviceCache[cacheKey] != device) {
            deviceCache[cacheKey] = device
            onChangeEvent.emit(Unit)
        }
    }

    override suspend fun onAdbEmulatorLost(deviceSerial: String, emulatorPort: Int) = mutex.withLock {
        val cacheKey = "adb:$deviceSerial:$emulatorPort"
        deviceCache[cacheKey]?.let {
            deviceCache[cacheKey] = it.copy(state = DetectedDevice.State.Removed)
            onChangeEvent.emit(Unit)
        }
    }.let { /* no-op */ }

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
