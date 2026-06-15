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
    private var uptimeSeconds = 0

    init {
        viewModelScope.launch {
            reloadData()
            launch { pollRequests() }
            launch { pollOverrides() }
            launch { pollUptime() }
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
            monitorLogsUseCase.getMonitorLogs(device).onSuccess { logs ->
                latestRequestCount = logs.count()
                updateSessionStats()
            }
            delay(1_000)
        }
    }

    private suspend fun pollOverrides() {
        while (true) {
            endpointsService.fetchAllEndpointConfigs(device).onSuccess { list ->
                latestOverridesCount = list.count { it.shouldFail == true || it.delayMs != null || it.appliedPresetOverride != null }
                updateSessionStats()
            }
            delay(5_000)
        }
    }

    private suspend fun pollUptime() {
        while (true) {
            delay(1_000)
            uptimeSeconds++
            updateSessionStats()
        }
    }

    private fun updateSessionStats() {
        state.update { current ->
            (current as? State.DisplayMetaData)?.copy(
                requestCount = latestRequestCount,
                overridesCount = latestOverridesCount,
                uptime = formatUptime(uptimeSeconds)
            ) ?: current
        }
    }

    private fun formatUptime(totalSeconds: Int): String {
        val hours = totalSeconds / SECONDS_IN_HOUR
        val minutes = (totalSeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
        val seconds = totalSeconds % SECONDS_IN_MINUTE
        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }

    @Immutable
    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class DisplayMetaData(
            val metaData: MetaData,
            val requestCount: Int? = null,
            val uptime: String? = null,
            val overridesCount: Int? = null,
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
                        uptime == other.uptime &&
                        overridesCount == other.overridesCount &&
                        (appIconBytes == null && other.appIconBytes == null ||
                                appIconBytes != null && other.appIconBytes != null &&
                                        appIconBytes.contentEquals(other.appIconBytes))
            }

            @Suppress("SAY_NO_TO_VAR")
            override fun hashCode(): Int {
                var result = metaData.hashCode()
                result = 31 * result + (requestCount ?: 0)
                result = 31 * result + (uptime?.hashCode() ?: 0)
                result = 31 * result + (overridesCount ?: 0)
                result = 31 * result + (appIconBytes?.contentHashCode() ?: 0)
                return result
            }
        }
    }

    private companion object {
        const val SECONDS_IN_HOUR = 3600
        const val SECONDS_IN_MINUTE = 60
    }
}
