package com.apadmi.mockzilla.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

actual open class ViewModel actual constructor(
    scope: CoroutineScope?
) {
    actual val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
}
private fun createViewModelScope(): CoroutineScope = MainScope()
