package com.apadmi.mockzilla.desktop.ui.widgets.misccontrols

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.utils.launchUnit
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope

class MiscControlsViewModel(
    private val device: Device?,
    private val eventBus: EventBus,
    private val clearingService: MockzillaManagement.CacheClearingService,
    scope: CoroutineScope? = null
): ViewModel(scope) {

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