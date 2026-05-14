package com.apadmi.mockzilla.ui.ui.common.widgets.metadata

import androidx.compose.runtime.Immutable

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MetaDataWidgetViewModel(
    private val device: Device,
    private val metaDataUseCase: MetaDataUseCase,
    private val monitorLogsUseCase: MonitorLogsUseCase,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)

    private var latestRequestCount: Int? = null

    init {
        viewModelScope.launch {
            reloadData()
            launch { pollRequests() }
        }
    }

    private suspend fun reloadData() {
        state.value = metaDataUseCase.getMetaData(device).fold(
            onSuccess = { State.DisplayMetaData(it) },
            onFailure = { State.Error }
        )
    }

    private suspend fun pollRequests() {
        while (true) {
            monitorLogsUseCase.getMonitorLogs(device).onSuccess { logs ->
                latestRequestCount = logs.count()
                updateSessionStats()
            }
            delay(1_000)
        }
    }

    private fun updateSessionStats() {
        state.update { current ->
            (current as? State.DisplayMetaData)?.copy(
                requestCount = latestRequestCount
            ) ?: current
        }
    }

    @Immutable
    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class DisplayMetaData(
            val metaData: MetaData,
            val requestCount: Int? = null,
        ) : State()
    }
}

