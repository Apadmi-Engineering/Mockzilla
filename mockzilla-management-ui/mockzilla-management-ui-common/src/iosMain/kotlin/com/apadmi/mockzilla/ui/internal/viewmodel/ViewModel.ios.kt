package com.apadmi.mockzilla.ui.internal.viewmodel

import com.apadmi.mockzilla.lib.InternalMockzillaApi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@InternalMockzillaApi
public actual open class ViewModel actual constructor(
    scope: CoroutineScope?
) {
    public actual val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
}
private fun createViewModelScope(): CoroutineScope = MainScope()
