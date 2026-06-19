package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MonitorLogDetailsViewModel(
    private val device: Device,
    private val logEvent: LogEvent?,
    private val monitorLogsUseCase: MonitorLogsUseCase,
    scope: CoroutineScope? = null,
) : ViewModel(scope) {
    val state: MutableStateFlow<State> = MutableStateFlow(
        if (logEvent == null) State.Empty
        else State.ViewDetails(
            logEvent = logEvent,
            requestBodyState = State.BodyState.from(logEvent.requestBody, logEvent.isRequestBodyTruncated),
            responseBodyState = State.BodyState.from(logEvent.responseBody, logEvent.isResponseBodyTruncated),
        )
    )

    init {
        if (logEvent != null && (logEvent.isRequestBodyTruncated || logEvent.isResponseBodyTruncated)) {
            viewModelScope.launch { loadFullBodies() }
        }
    }

    private suspend fun loadFullBodies() {
        println("LOADING FULL BODIES")
        monitorLogsUseCase.fetchLogDetail(device, logEvent!!.id)
            .onSuccess { full ->
                state.update { current ->
                    (current as? State.ViewDetails)?.copy(
                        requestBodyState = State.BodyState.Available(full.requestBody),
                        responseBodyState = State.BodyState.Available(full.responseBody),
                    ) ?: current
                }
            }
            .onFailure {
                state.update { current ->
                    (current as? State.ViewDetails)?.copy(
                        requestBodyState = if (logEvent.isRequestBodyTruncated)
                            State.BodyState.Error(logEvent.requestBody)
                        else current.requestBodyState,
                        responseBodyState = if (logEvent.isResponseBodyTruncated)
                            State.BodyState.Error(logEvent.responseBody)
                        else current.responseBodyState,
                    ) ?: current
                }
            }
    }

    fun onTabSelected(tab: State.ViewDetails.Tab) {
        state.update { current ->
            (current as? State.ViewDetails)?.copy(selectedTab = tab) ?: current
        }
    }

    sealed class State {
        sealed class BodyState(val bodyOrPreview: String) {
            data class Available(val text: String) : BodyState(text)
            data class Loading(val preview: String) : BodyState(preview)
            data class Error(val preview: String) : BodyState(preview)

            companion object {
                fun from(body: String, truncated: Boolean): BodyState =
                    if (truncated) Loading(body) else Available(body)
            }
        }

        data object Empty : State()

        /**
         * @property logEvent
         * @property selectedTab
         * @property requestBodyState
         * @property responseBodyState
         */
        data class ViewDetails(
            val logEvent: LogEvent,
            val selectedTab: Tab = Tab.Response,
            val requestBodyState: BodyState = BodyState.Available(""),
            val responseBodyState: BodyState = BodyState.Available(""),
        ) : State() {
            enum class Tab {
                Request, Response
            }
        }
    }
}
