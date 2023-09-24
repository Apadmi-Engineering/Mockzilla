package com.apadmi.mockzilla.desktop.ui.viewmodel


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

private fun createViewModelScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

abstract class ViewModel<StateType>(
    scope: CoroutineScope? = null
) {
    val viewModelScope: CoroutineScope = scope ?: createViewModelScope()

    abstract val state: StateFlow<StateType>
}