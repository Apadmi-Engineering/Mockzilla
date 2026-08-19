@file:NoKDoc

package com.apadmi.mockzilla.desktop.ui.tools
import com.apadmi.mockzilla.desktop.engine.tools.CodeGenUseCase
import com.apadmi.mockzilla.lib.NoKDoc
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class CodeGenViewModel(
    private val codeGenUseCase: CodeGenUseCase,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Inputting)

    fun generateConfig(inputPath: String, outputPath: String) {
        state.value = State.Loading
        viewModelScope.launch {
            state.value = codeGenUseCase.generateConfig(inputPath, outputPath).fold(
                onSuccess = { State.Inputting },
                onFailure = { State.Error }
            )
        }
    }

    sealed class State() {
        data object Inputting : State()
        data object Loading : State()
        data object Error : State()
    }
}

