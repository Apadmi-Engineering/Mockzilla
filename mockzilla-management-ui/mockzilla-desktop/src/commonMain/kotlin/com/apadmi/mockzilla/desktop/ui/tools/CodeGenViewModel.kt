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

    private val validInputType = arrayOf(".yaml", ".yml", ".json")
    private val validOutputType = arrayOf(".dart")

    fun generateConfig(inputPath: String, outputPath: String) {
        state.value = State.Loading
        val inputValid = isInputValid(inputPath)
        val outputValid = isOutputValid(outputPath)

        if (inputValid && outputValid) {
            viewModelScope.launch {
                state.value = codeGenUseCase.generateConfig(inputPath, outputPath).fold(
                    onSuccess = { State.Success },
                    onFailure = { err -> State.GeneratorError(err) }
                )
            }
        } else {
            state.value = State.InputError(
                !inputValid, !outputValid
            )
        }
    }

    fun updatedText() {
        state.value = State.Inputting
    }

    private fun isInputValid(inputPath: String): Boolean {
        return validInputType.any { inputPath.endsWith(it) }
    }

    private fun isOutputValid(outputPath: String): Boolean {
        return validOutputType.any { outputPath.endsWith(it) }
    }

    sealed class State {
        data object Inputting : State()
        data object Loading : State()
        data object Success : State()
        data class GeneratorError(val err: Throwable) : State()
        data class InputError(val inputInvalid: Boolean, val outputInvalid: Boolean) : State()

        val isError: Boolean
            get() {
                return this is GeneratorError || this is InputError
            }
    }
}


