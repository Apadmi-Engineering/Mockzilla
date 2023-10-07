package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

expect abstract class ViewModel<StateType>(
    scope: CoroutineScope? = null
) {
    val viewModelScope: CoroutineScope

    abstract val state: StateFlow<StateType>
}