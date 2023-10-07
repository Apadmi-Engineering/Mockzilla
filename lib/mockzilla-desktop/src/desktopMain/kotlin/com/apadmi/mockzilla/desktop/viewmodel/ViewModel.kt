package com.apadmi.mockzilla.desktop.viewmodel

import org.koin.java.KoinJavaComponent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

actual abstract class ViewModel<StateType> actual constructor(
    scope: CoroutineScope?
) {
    actual val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
    actual abstract val state: StateFlow<StateType>
}

inline fun <reified T : Any> getViewModel() = KoinJavaComponent.getKoin().get<T>()

private fun createViewModelScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)
