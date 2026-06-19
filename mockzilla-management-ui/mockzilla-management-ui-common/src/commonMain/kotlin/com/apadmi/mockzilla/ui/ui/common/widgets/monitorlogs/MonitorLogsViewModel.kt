package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.utils.launchUnit
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class MonitorLogsViewModel(
    private val device: Device,
    private val monitorLogsUseCase: MonitorLogsUseCase,
    private val activeDeviceSelector: ActiveDeviceSelector,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow(State.DisplayLogs(emptyList()))
    private var pollingJob: Job? = null

    init {
        pollForLogs(device)
    }

    private fun pollForLogs(device: Device) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                monitorLogsUseCase.getMonitorLogs(device)
                    .onSuccess { result ->
                        state.value = State.DisplayLogs(result.logs)
                        activeDeviceSelector.onLogPollSuccess(device, result.appPackage)
                    }
                    .onFailure { activeDeviceSelector.onLogPollFailure(device) }

                delay(200)
            }
        }
    }

    fun clearLogs() = viewModelScope.launchUnit {
        monitorLogsUseCase.clearMonitorLogs(device).onSuccess {
            pollForLogs(device)
        }
    }

    sealed class State {
        /**
         * @property entries
         */
        data class DisplayLogs(val entries: List<LogEvent>) : State()
    }
}
