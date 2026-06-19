package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.ui.engine.Config

import io.github.z4kn4fein.semver.toVersion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
    fun removeDevice(device: Device)
    fun onLogPollSuccess(device: Device, appPackage: String)
    fun onLogPollFailure(device: Device)
}

internal class ActiveDeviceManagerImpl(
    private val metaDataUseCase: MetaDataUseCase,
    private val scope: CoroutineScope
) : ActiveDeviceMonitor, ActiveDeviceSelector {
    override val selectedDevice = MutableStateFlow<StatefulDevice?>(null)
    override val onDeviceConnectionStateChange = MutableSharedFlow<Unit>(replay = 1)
    private val allDevicesInternal = mutableMapOf<Device, StatefulDevice>()
    override val allDevices get() = allDevicesInternal.values

    init {
        scope.launch { onDeviceConnectionStateChange.emit(Unit) }
    }

    override fun onLogPollSuccess(device: Device, appPackage: String) {
        val current = allDevicesInternal[device] ?: return
        val appPackageChanged = current.metaData.appPackage != appPackage
        val wasDisconnected = !current.isConnected
        if (!appPackageChanged && !wasDisconnected) return

        scope.launch {
            metaDataUseCase.getMetaData(device).onSuccess { metaData ->
                val updated = current.copy(
                    metaData = metaData,
                    isConnected = true,
                    isCompatibleMockzillaVersion = metaData.mockzillaVersion.toVersion() >= Config.minSupportedMockzillaVersion
                )
                allDevicesInternal[device] = updated
                if (device == selectedDevice.value?.device) {
                    selectedDevice.value = updated
                }
                onDeviceConnectionStateChange.emit(Unit)
            }
        }
    }

    override fun onLogPollFailure(device: Device) {
        val current = allDevicesInternal[device] ?: return
        if (!current.isConnected) return

        val updated = current.copy(isConnected = false)
        allDevicesInternal[device] = updated
        if (device == selectedDevice.value?.device) {
            selectedDevice.value = updated
        }
        scope.launch { onDeviceConnectionStateChange.emit(Unit) }
    }

    override fun setActiveDeviceWithMetaData(device: Device, metadata: MetaData) {
        allDevicesInternal[device] = StatefulDevice(
            device = device,
            metaData = metadata,
            isConnected = true,
            isCompatibleMockzillaVersion = metadata.mockzillaVersion.toVersion() >= Config.minSupportedMockzillaVersion
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

    override fun removeDevice(device: Device) {
        scope.launch {
            if (selectedDevice.value?.device == device) {
                clearSelectedDevice()
            }
            allDevicesInternal.remove(device)
            onDeviceConnectionStateChange.emit(Unit)
        }
    }

}
