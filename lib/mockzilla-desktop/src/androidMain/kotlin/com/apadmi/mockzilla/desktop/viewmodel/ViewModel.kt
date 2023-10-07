package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

actual abstract class ViewModel<StateType> actual constructor(
    scope: CoroutineScope?
): AndroidXViewModel() {
    actual val viewModelScope: CoroutineScope = scope ?: androidXViewModelScope
    actual abstract val state: StateFlow<StateType>
}