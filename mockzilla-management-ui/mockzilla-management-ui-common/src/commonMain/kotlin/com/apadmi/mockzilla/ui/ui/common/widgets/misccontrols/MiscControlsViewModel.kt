package com.apadmi.mockzilla.ui.ui.common.widgets.misccontrols

import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.utils.launchUnit
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope

class MiscControlsViewModel(
    private val device: Device?,
    private val eventBus: EventBus,
    private val clearingService: MockzillaManagement.CacheClearingService,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    fun refreshAllData() {
        eventBus.send(EventBus.Event.FullRefresh)
    }

    fun clearAllOverrides() = viewModelScope.launchUnit {
        val device = this@MiscControlsViewModel.device ?: return@launchUnit
        clearingService.clearAllCaches(device).onSuccess {
            eventBus.send(EventBus.Event.FullRefresh)
        }
    }
}
