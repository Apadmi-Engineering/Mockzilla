package com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection

import androidx.compose.runtime.Immutable
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorUseCase
import com.apadmi.mockzilla.desktop.engine.connection.DetectedDevice
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCase
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.models.MetaData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class DeviceConnectionViewModel(
    private val metaDataUseCase: MetaDataUseCase,
    private val activeDeviceSelector: ActiveDeviceSelector,
    private val adbConnectorUseCase: AdbConnectorUseCase,
    private val deviceDetectionUseCase: DeviceDetectionUseCase
) : ViewModel() {
    val state = MutableStateFlow(State())
    private var connectionJob: Job? = null

    init {
        deviceDetectionUseCase.onChangeEvent.onEach {
            state.value = state.value.copy(devices = deviceDetectionUseCase.devices)
        }.launchIn(viewModelScope)
    }

    // TODO: Replace this with better strategies of device connections
    // This is just a rough and ready way to get the UI to be testable
    fun onIpAndPortChanged(newValue: String) {
        connectionJob?.cancel()
        val device = createDeviceOrNull(newValue)
        device ?: run {
            state.value = state.value.copy(
                ipAndPort = newValue,
                connectionState = State.ConnectionState.Disconnected
            )
            return
        }

        state.value = state.value.copy(
            ipAndPort = newValue,
            connectionState = State.ConnectionState.Connecting
        )
        connectionJob = awaitConnectionAndSetState(device)
    }

    fun connectToDevice(device: DetectedDevice) = viewModelScope.launch {
        // TODO: Handle error here
        val address = deviceDetectionUseCase.prepareForConnection(device).getOrNull()!!
        onIpAndPortChanged(address.raw)
    }

    private fun createDeviceOrNull(ipAndPort: String): Device? {
        val split = ipAndPort.split(":").takeIf { it.size == 2 } ?: return null
        return Device(split[0], split[1])
    }

    private fun awaitConnectionAndSetState(device: Device): Job = viewModelScope.launch {
        do {
            yield()
            state.value = state.value.copy(
                connectionState = metaDataUseCase.getMetaData(device)
                    .onSuccess { onSuccessfulConnection(device, it) }
                    .fold(onSuccess = { State.ConnectionState.Connected },
                        onFailure = { State.ConnectionState.Connecting })
            )
            delay(300)
        } while (state.value.connectionState != State.ConnectionState.Connected)
    }

    private fun onSuccessfulConnection(tmpDevice: Device, metaData: MetaData) {
        activeDeviceSelector.setActiveDeviceWithMetaData(tmpDevice, metaData)
    }

    @Immutable
    data class State(
        val ipAndPort: String = "",
        val connectionState: ConnectionState = ConnectionState.Disconnected,
        val devices: List<DetectedDevice> = emptyList()
    ) {
        enum class ConnectionState {
            Connected,
            Connecting,
            Disconnected,
            ;
        }
    }
}
