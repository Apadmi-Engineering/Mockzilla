package com.apadmi.mockzilla.desktop.ui.viewmodel

import org.koin.java.KoinJavaComponent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

abstract class ViewModel<StateType>(
    scope: CoroutineScope? = null
) {
    val viewModelScope: CoroutineScope = scope ?: createViewModelScope()
    abstract val state: StateFlow<StateType>
}

inline fun <reified T : Any> getViewModel() = KoinJavaComponent.getKoin().get<T>()

private fun createViewModelScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)
