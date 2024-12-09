package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.desktop.engine.Config
import com.apadmi.mockzilla.lib.models.MetaData
import com.vdurmont.semver4j.Semver

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

interface ActiveDeviceMonitor {
    val selectedDevice: StateFlow<StatefulDevice?>

    // Fires when a device connects / disconnects
    val onDeviceConnectionStateChange: Flow<Unit>
    val allDevices: Collection<StatefulDevice>
}

interface ActiveDeviceSelector {
    fun clearSelectedDevice()
    fun setActiveDeviceWithMetaData(device: Device, metadata: MetaData)
    fun updateSelectedDevice(device: Device)
}

class ActiveDeviceManagerImpl(
    private val metaDataUseCase: MetaDataUseCase,
    scope: CoroutineScope
) : ActiveDeviceMonitor, ActiveDeviceSelector {
    override val selectedDevice = MutableStateFlow<StatefulDevice?>(null)
    override val onDeviceConnectionStateChange = MutableSharedFlow<Unit>(replay = 1)
    private val allDevicesInternal = mutableMapOf<Device, StatefulDevice>()
    override val allDevices get() = allDevicesInternal.values

    private var pollingJob: Job? = null

    init {
        pollingJob = scope.launch {
            monitorDeviceConnections()
        }
    }

    // TODO: Hopefully this will eventually become a websocket
    private suspend fun monitorDeviceConnections() = coroutineScope {
        while (true) {
            allDevicesInternal.forEach { (device, statefulDevice) ->
                val newStatefulDevice =
                    metaDataUseCase.getMetaData(device, isPolling = true).fold(onSuccess = { metaData ->
                        statefulDevice.copy(
                            isConnected = true,
                            connectedAppPackage = metaData.appPackage
                        )
                    }, onFailure = {
                        statefulDevice.copy(isConnected = false)
                    })

                if (statefulDevice != newStatefulDevice) {
                    onDeviceConnectionStateChange.emit(Unit)
                    if (device == statefulDevice.device) {
                        selectedDevice.value = newStatefulDevice
                    }
                }
                allDevicesInternal[device] = newStatefulDevice
            }

            if (onDeviceConnectionStateChange.replayCache.isEmpty()) {
                onDeviceConnectionStateChange.emit(Unit)
            }

            delay(1.seconds)
            yield()
        }
    }
    override fun setActiveDeviceWithMetaData(device: Device, metadata: MetaData) {
        allDevicesInternal[device] = StatefulDevice(
            device = device,
            name = "${metadata.runTarget ?: metadata.appPackage}-${metadata.deviceModel}",
            isConnected = true,
            connectedAppPackage = metadata.appPackage,
            isCompatibleMockzillaVersion = Semver(metadata.mockzillaVersion).isGreaterThanOrEqualTo(Config.minSupportedMockzillaVersion)
        ).also {
            selectedDevice.value = it
        }
    }

    override fun updateSelectedDevice(device: Device) {
        selectedDevice.value = allDevicesInternal[device]
    }

    override fun clearSelectedDevice() {
        selectedDevice.value = null
    }

    // Only used by tests, otherwise should survive for the lifetime of the application i.e. the lifetime
    // of the injected scope
    internal fun cancelPolling() {
        pollingJob?.cancel()
    }
}
