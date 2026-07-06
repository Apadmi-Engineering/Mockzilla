package com.apadmi.mockzilla.ui.internal.viewmodel

import com.apadmi.mockzilla.lib.InternalMockzillaApi

import kotlinx.coroutines.CoroutineScope

@InternalMockzillaApi
public expect open class ViewModel(
    scope: CoroutineScope? = null
) {
    public val viewModelScope: CoroutineScope
}
