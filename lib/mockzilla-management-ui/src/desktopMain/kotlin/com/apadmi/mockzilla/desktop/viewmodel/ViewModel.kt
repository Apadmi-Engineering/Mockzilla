package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.skiko.MainUIDispatcher

actual open class ViewModel actual constructor(
    scope: CoroutineScope?
) {
    actual val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
}
private fun createViewModelScope(): CoroutineScope = CoroutineScope(MainUIDispatcher)
