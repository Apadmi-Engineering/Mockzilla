package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

actual abstract class ViewModel actual constructor(
    scope: CoroutineScope?
) {
    actual val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
//    actual abstract val state: StateFlow<StateType>
}
private fun createViewModelScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)
