@file:NoKDoc

package com.apadmi.mockzilla.desktop.ui.tools

import com.apadmi.mockzilla.lib.NoKDoc
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

internal class CodeGenViewModel(
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Inputting)

    sealed class State() {
        data object Inputting : State()
        data object Loading : State()
        data object Error : State()
    }
}
