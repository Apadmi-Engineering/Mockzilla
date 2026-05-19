package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import com.apadmi.mockzilla.ui.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MonitorLogDetailsViewModel(
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow(State.ViewDetails())

    fun onTabSelected(tab: State.ViewDetails.Tab) = state.update { it.copy(selectedTab = tab) }

    sealed class State {
        /**
         * @property selectedTab
         */
        data class ViewDetails(
            val selectedTab: Tab = Tab.Response,
        ) : State() {
            enum class Tab {
                Request, Response
            }
        }
    }
}
