package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MonitorLogsViewModel(
    private val monitorLogsUseCase: MonitorLogsUseCase,
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow<State>(State.Empty)
    private var pollingJob: Job? = null

    override suspend fun reloadData(selectedDevice: Device?) {
        val device = selectedDevice ?: return
        pollForLogs(device)

    }

    private fun pollForLogs(device: Device) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                monitorLogsUseCase.getMonitorLogs(device).onSuccess { logs ->
                    state.value = if (logs.none()) {
                        State.Empty
                    } else {
                        State.DisplayLogs(logs)
                    }
                }

                delay(200)
            }
        }
    }

    sealed class State {
        data class DisplayLogs(val entries: Sequence<LogEvent>): State()
        object Empty: State()
    }
}
