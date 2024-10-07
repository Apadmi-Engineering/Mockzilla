package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MonitorLogsViewModel(
    device: Device,
    private val monitorLogsUseCase: MonitorLogsUseCase,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.DisplayLogs(emptySequence()))
    private var pollingJob: Job? = null

    init {
        pollForLogs(device)
    }

    private fun pollForLogs(device: Device) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                monitorLogsUseCase.getMonitorLogs(device).onSuccess { logs ->
                    state.value = State.DisplayLogs(logs)
                }

                delay(200)
            }
        }
    }

    sealed class State {
        /**
         * @property entries
         */
        data class DisplayLogs(val entries: Sequence<LogEvent>) : State()
    }
}
