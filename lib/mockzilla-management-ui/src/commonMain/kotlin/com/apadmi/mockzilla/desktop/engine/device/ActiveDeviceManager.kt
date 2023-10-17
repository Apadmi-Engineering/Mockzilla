package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.lib.models.MetaData

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

interface ActiveDeviceMonitor {
    val onDeviceSelectionChange: SharedFlow<Unit>
    val onDeviceConnectionStateChange: SharedFlow<Unit>
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

    override var activeDevice: Device? = null
        private set(value) {
            field = value
            notifyDeviceChange()
        }

    init {
        // TODO: Hopefully this will eventually become a websocket
        scope.launch {
            monitorDeviceConnections()
        }
    }

    private suspend fun monitorDeviceConnections() {
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
}
