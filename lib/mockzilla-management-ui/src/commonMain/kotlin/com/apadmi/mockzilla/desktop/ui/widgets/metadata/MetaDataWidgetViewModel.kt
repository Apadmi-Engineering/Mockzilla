package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import androidx.compose.runtime.Immutable
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel
import com.apadmi.mockzilla.lib.models.MetaData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class MetaDataWidgetViewModel(
    private val metaDataUseCase: MetaDataUseCase,
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow<State>(State.NoDeviceConnected)

    override suspend fun reloadData() {
        state.value = getMetaData()
    }

    private suspend fun getMetaData() = activeDeviceOrDefault(State.NoDeviceConnected) { device ->
        metaDataUseCase.getMetaData(device).fold(onSuccess = {
            State.DisplayMetaData(it)
        }, onFailure = {
            if (state.value is State.DisplayMetaData) {
                state.value
            } else {
                State.NoDeviceConnected
            }
        })
    }

    @Immutable
    sealed class State {
        object NoDeviceConnected : State()
        data class DisplayMetaData(val metaData: MetaData) : State()
    }
}
