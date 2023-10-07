package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent
import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

actual abstract class ViewModel actual constructor(
    scope: CoroutineScope?
): AndroidXViewModel() {
    actual val viewModelScope: CoroutineScope = scope ?: androidXViewModelScope
//    actual abstract val state: StateFlow<StateType>
}
