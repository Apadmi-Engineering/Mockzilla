package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

expect abstract class ViewModel(
    scope: CoroutineScope? = null
) {
    val viewModelScope: CoroutineScope

//    abstract val state: StateFlow<StateType>
}

