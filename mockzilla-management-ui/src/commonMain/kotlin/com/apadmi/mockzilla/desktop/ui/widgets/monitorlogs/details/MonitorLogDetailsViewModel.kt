package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.details

import com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.MonitorLogsViewModel.State
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MonitorLogDetailsViewModel(
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow(
        State.ViewDetails(
            requestHeaders = false,
            requestBody = false,
            responseHeaders = false,
            responseBody = true
        )
    )

    fun onViewRequestHeaders(view: Boolean) = updateState { state ->
        state.copy(requestHeaders = view)
    }

    fun onViewRequestBody(view: Boolean) = updateState { state ->
        state.copy(requestBody = view)
    }

    fun onViewResponseHeaders(view: Boolean) = updateState { state ->
        state.copy(responseHeaders = view)
    }

    fun onViewResponseBody(view: Boolean) = updateState { state ->
        state.copy(responseBody = view)
    }

    private fun updateState(
        update: (State.ViewDetails) -> State.ViewDetails
    ) = state.update(update)

    sealed class State {
        /**
         * @property requestHeaders
         * @property requestBody
         * @property responseHeaders
         * @property responseBody
         */
        data class ViewDetails(
            val requestHeaders: Boolean,
            val requestBody: Boolean,
            val responseHeaders: Boolean,
            val responseBody: Boolean,
        ) : State()
    }
}
