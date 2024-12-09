package com.apadmi.mockzilla.desktop.viewmodel

import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

import kotlinx.coroutines.CoroutineScope

actual open class ViewModel actual constructor(
    scope: CoroutineScope?
) : AndroidXViewModel() {
    actual val viewModelScope: CoroutineScope = scope ?: androidXViewModelScope
}
