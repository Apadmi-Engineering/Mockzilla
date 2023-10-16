package com.apadmi.mockzilla.desktop.viewmodel

import org.jetbrains.skiko.MainUIDispatcher

import kotlinx.coroutines.CoroutineScope

actual open class ViewModel actual constructor(
    scope: CoroutineScope?
) {
    actual val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
}
private fun createViewModelScope(): CoroutineScope = CoroutineScope(MainUIDispatcher)
