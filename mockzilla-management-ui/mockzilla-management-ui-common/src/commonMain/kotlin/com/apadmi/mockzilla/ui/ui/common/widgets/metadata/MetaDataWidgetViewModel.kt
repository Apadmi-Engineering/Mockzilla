@file:NoKDoc

package com.apadmi.mockzilla.ui.ui.common.widgets.metadata

import androidx.compose.runtime.Immutable

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.ui.engine.device.AppIconUseCase
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.internal.NoKDoc
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MetaDataWidgetViewModel(
    private val device: Device,
    private val metaDataUseCase: MetaDataUseCase,
    private val monitorLogsUseCase: MonitorLogsUseCase,
    private val appIconUseCase: AppIconUseCase,
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
        val metaDataResult = metaDataUseCase.getMetaData(device)
        val iconResult = appIconUseCase.getAppIcon(device)
        state.value = metaDataResult.fold(
            onSuccess = { State.DisplayMetaData(it, appIconBytes = iconResult.getOrNull()) },
            onFailure = { State.Error }
        )
    }

    private suspend fun pollRequests() {
        while (true) {
            monitorLogsUseCase.getMonitorLogs(device).onSuccess { result ->
                latestRequestCount = result.logs.size
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
            val appIconBytes: ByteArray? = null,
        ) : State() {
            override fun equals(other: Any?): Boolean {
                if (this === other) {
                    return true
                }
                if (other !is DisplayMetaData) {
                    return false
                }
                return metaData == other.metaData &&
                        requestCount == other.requestCount &&
                        (appIconBytes == null && other.appIconBytes == null ||
                                appIconBytes != null && other.appIconBytes != null &&
                                        appIconBytes.contentEquals(other.appIconBytes))
            }

            @Suppress("SAY_NO_TO_VAR")
            override fun hashCode(): Int {
                var result = metaData.hashCode()
                result = 31 * result + (requestCount ?: 0)
                result = 31 * result + (appIconBytes?.contentHashCode() ?: 0)
                return result
            }
        }
    }
}
