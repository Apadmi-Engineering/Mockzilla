package com.apadmi.mockzilla.desktop.ui.widgets.misccontrols

import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.CoroutineScope

class MiscControlsViewModel(
    private val eventBus: EventBus,
    private val clearingService: MockzillaManagement.CacheClearingService,
    scope: CoroutineScope? = null
): ViewModel(scope) {

    fun refreshAllData() {
        eventBus.send(EventBus.Event.FullRefresh)
    }

    fun clearAllOverrides() {
//        clearingService.clearAllCaches()
        eventBus.send(EventBus.Event.FullRefresh)
    }
}