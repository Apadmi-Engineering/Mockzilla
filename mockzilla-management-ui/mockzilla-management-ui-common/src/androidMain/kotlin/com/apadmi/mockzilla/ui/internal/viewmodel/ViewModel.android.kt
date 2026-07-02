package com.apadmi.mockzilla.ui.internal.viewmodel

import com.apadmi.mockzilla.lib.InternalMockzillaApi

import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidXViewModelScope

import kotlinx.coroutines.CoroutineScope

@InternalMockzillaApi
public actual open class ViewModel actual constructor(
    scope: CoroutineScope?
) : AndroidXViewModel() {
    public actual val viewModelScope: CoroutineScope = scope ?: androidXViewModelScope
}
