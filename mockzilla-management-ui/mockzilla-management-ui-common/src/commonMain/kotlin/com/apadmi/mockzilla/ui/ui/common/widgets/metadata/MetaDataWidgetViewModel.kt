package com.apadmi.mockzilla.ui.ui.common.widgets.metadata

import androidx.compose.runtime.Immutable

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.AppIconUseCase
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
    private val appIconUseCase: AppIconUseCase,
    private val endpointsService: MockzillaManagement.EndpointsService,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)
    private var latestRequestCount: Int? = null
    private var latestOverridesCount: Int? = null

    init {
        viewModelScope.launch {
            reloadData()
            launch { pollRequests() }
            launch { pollOverrides() }
        }
    }

    private suspend fun reloadData() {
        val metaDataResult = metaDataUseCase.getMetaData(device)
        val iconResult = appIconUseCase.getAppIcon(device)
        val overridesResult = endpointsService.fetchAllEndpointConfigs(device)

        state.value = metaDataResult.fold(
            onSuccess = {
                State.DisplayMetaData(
                    it,
                    requestCount = latestRequestCount,
                    overridesCount = overridesResult.getOrNull()?.countOverrides() ?: 0,
                    appIconBytes = iconResult.getOrNull()
                )
            },
            onFailure = { State.Error }
        )
    }

    private fun List<com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig>.countOverrides() =
        count { it.shouldFail != null || it.delayMs != null || it.appliedPresetOverride != null }

    private suspend fun pollRequests() {
        while (true) {
            monitorLogsUseCase.getMonitorLogs(device).onSuccess { logs ->
                latestRequestCount = logs.count()
                updateSessionStats()
            }
            delay(1_000)
        }
    }

    private suspend fun pollOverrides() {
        while (true) {
            endpointsService.fetchAllEndpointConfigs(device).onSuccess { endpoints ->
                latestOverridesCount = endpoints.countOverrides()
                updateSessionStats()
            }
            delay(1_000)
        }
    }

    private fun updateSessionStats() {
        state.update { current ->
            (current as? State.DisplayMetaData)?.copy(
                requestCount = latestRequestCount,
                overridesCount = latestOverridesCount ?: (current as? State.DisplayMetaData)?.overridesCount ?: 0
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
            val overridesCount: Int = 0,
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
                        overridesCount == other.overridesCount &&
                        (appIconBytes == null && other.appIconBytes == null ||
                                appIconBytes != null && other.appIconBytes != null &&
                                        appIconBytes.contentEquals(other.appIconBytes))
            }

            @Suppress("SAY_NO_TO_VAR")
            override fun hashCode(): Int {
                var result = metaData.hashCode()
                result = 31 * result + (requestCount ?: 0)
                result = 31 * result + overridesCount
                result = 31 * result + (appIconBytes?.contentHashCode() ?: 0)
                return result
            }
        }
    }
}
