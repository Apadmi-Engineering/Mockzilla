package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.lib.models.MetaData

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

interface ActiveDeviceMonitor {
    // Fires each time the user changes the currently selected device (or connected app package changes)
    val onDeviceSelectionChange: Flow<Unit>

    // Fires when a device connects / disconnects
    val onDeviceConnectionStateChange: Flow<Unit>
    val activeDevice: Device?
    val allDevices: Collection<StatefulDevice>
}

interface ActiveDeviceSelector {
    fun clearActiveDevice()
    fun setActiveDeviceWithMetaData(device: Device, metadata: MetaData)
    fun updateActiveDevice(device: Device)
}

class ActiveDeviceManagerImpl(
    private val metaDataUseCase: MetaDataUseCase,
    private val scope: CoroutineScope
) : ActiveDeviceMonitor, ActiveDeviceSelector {
    override val onDeviceSelectionChange = MutableSharedFlow<Unit>()
    override val onDeviceConnectionStateChange = MutableSharedFlow<Unit>()
    private val allDevicesInternal = mutableMapOf<Device, StatefulDevice>()
    override val allDevices get() = allDevicesInternal.values

    private var pollingJob: Job? = null

    override var activeDevice: Device? = null
        private set(value) {
            field = value
            notifyDeviceChange()
        }

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
                    metaDataUseCase.getMetaData(device).fold(onSuccess = { metaData ->
                        statefulDevice.copy(
                            isConnected = true,
                            connectedAppPackage = metaData.appPackage
                        )
                    }, onFailure = {
                        statefulDevice.copy(isConnected = false)
                    })

                if (statefulDevice != newStatefulDevice) {
                    onDeviceConnectionStateChange.emit(Unit)
                    if (device == activeDevice) {
                        notifyDeviceChange()
                    }
                }
                allDevicesInternal[device] = newStatefulDevice
            }

            delay(0.5.seconds)
            yield()
        }
    }

    private fun notifyDeviceChange() = scope.launch {
        onDeviceSelectionChange.emit(Unit)
    }

    override fun setActiveDeviceWithMetaData(device: Device, metadata: MetaData) {
        allDevicesInternal[device] = StatefulDevice(
            device = device,
            name = "${metadata.operatingSystem}-${metadata.deviceModel}",
            isConnected = true,
            connectedAppPackage = metadata.appPackage
        )
        activeDevice = device
    }

    override fun updateActiveDevice(device: Device) {
        activeDevice = device
    }

    override fun clearActiveDevice() {
        activeDevice = null
    }

    // Only used by tests, otherwise should survive for the lifetime of the application i.e. the lifetime
    // of the injected scope
    internal fun cancelPolling() {
        pollingJob?.cancel()
    }
}
