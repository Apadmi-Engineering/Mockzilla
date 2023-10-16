package com.apadmi.mockzilla.desktop.engine.device

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

interface ActiveDeviceMonitor {
    val onDeviceSelectionChange: SharedFlow<Unit>
    val activeDevice: Device?
}

interface ActiveDeviceSelector {
    fun setActiveDevice(device: Device)
}

class ActiveDeviceManagerImpl(
    private val scope: CoroutineScope
) : ActiveDeviceMonitor, ActiveDeviceSelector {
    override val onDeviceSelectionChange = MutableSharedFlow<Unit>()

    override var activeDevice: Device? = null; private set

    override fun setActiveDevice(device: Device) {
        activeDevice = device
        scope.launch {
            onDeviceSelectionChange.emit(Unit)
        }
    }
}