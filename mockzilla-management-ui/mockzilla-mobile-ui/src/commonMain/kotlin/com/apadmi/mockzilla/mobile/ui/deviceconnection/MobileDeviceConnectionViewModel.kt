package com.apadmi.mockzilla.mobile.ui.deviceconnection

import androidx.compose.runtime.Immutable

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.sharedstate.MockzillaSharedProcessState
import com.apadmi.mockzilla.lib.sharedstate.MockzillaSharedProcessStateHandler
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class MobileDeviceConnectionViewModel(
    private val metaDataUseCase: MetaDataUseCase,
    private val activeDeviceSelector: ActiveDeviceSelector,
    private val sharedProcessStateHandler: MockzillaSharedProcessStateHandler,
) : ViewModel() {
    val state = MutableStateFlow<State>(State.Connecting)
    private var connectionJob: Job? = null

    init {
        attemptLocalConnection()
    }

    fun attemptLocalConnection() = viewModelScope.launch {
        connectionJob?.cancel()
        state.value = State.Connecting
        val sharedState = sharedProcessStateHandler.getSharedProcessState()
        sharedState ?: run {
            state.value = State.Error("Mockzilla Server not running")
            return@launch
        }
        connectionJob = awaitConnectionAndSetState(sharedState.toDevice())
    }

    private fun awaitConnectionAndSetState(device: Device): Job = viewModelScope.launch {
        state.value = metaDataUseCase.getMetaData(device)
            .onSuccess { onSuccessfulConnection(device, it) }
            .fold(onSuccess = { State.Connected },
                onFailure = { State.Error(it.toString()) }
            )
    }

    private fun onSuccessfulConnection(tmpDevice: Device, metaData: MetaData) {
        activeDeviceSelector.setActiveDeviceWithMetaData(tmpDevice, metaData)
    }

    @Immutable
    sealed class State {
        data object Connected : State()
        data object Connecting : State()
        data class Error(val message: String) : State()
    }
}

private fun MockzillaSharedProcessState.toDevice() = Device(ip, port.toString())
