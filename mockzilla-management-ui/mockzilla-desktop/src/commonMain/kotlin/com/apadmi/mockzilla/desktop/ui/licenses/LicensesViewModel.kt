package com.apadmi.mockzilla.desktop.ui.licenses

import com.apadmi.mockzilla.desktop.engine.licenses.LibraryForAttribution
import com.apadmi.mockzilla.desktop.engine.licenses.LicensesUseCase
import com.apadmi.mockzilla.lib.MockzillaBuildConfig
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class LicensesViewModel(
    private val licensesUseCase: LicensesUseCase,
    isDevelopmentBuild: Boolean = MockzillaBuildConfig.isDevelopmentBuild,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)

    init {
        if (isDevelopmentBuild) {
            state.value = State.DevBuild
        } else {
            viewModelScope.launch {
                licensesUseCase.getLicenses()
                    .onSuccess { libs -> state.value = State.Populated(libs) }
                    .onFailure { state.value = State.ErrorLoading }
            }
        }
    }

    sealed class State {
        data object Loading : State()
        data object DevBuild : State()
        data object ErrorLoading : State()
        /**
         * @property libraries
         */
        data class Populated(val libraries: List<LibraryForAttribution>) : State()
    }
}
