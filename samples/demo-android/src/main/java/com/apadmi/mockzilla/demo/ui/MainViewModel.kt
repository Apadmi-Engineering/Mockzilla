package com.apadmi.mockzilla.demo.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apadmi.mockzilla.demo.engine.AnimalDto

import com.apadmi.mockzilla.demo.engine.Repository
import com.apadmi.mockzilla.demo.engine.network.DataResult

import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository,
    private val setReleaseMode: suspend (isRelease: Boolean) -> Unit
) : ViewModel() {
    val state = mutableStateOf<State>(State.Fetching())

    init {
        makeRequest(someValue = "")
    }

    fun makeRequest(someValue: String) = viewModelScope.launch {
        state.value = State.Fetching(values = state.value.values)

        val cowResult = repository.getAnimal(urlSuffix = "cow", someRequestValue = someValue)
        val sheepResult = repository.getAnimal(urlSuffix = "sheep", someRequestValue = someValue)
        val pigResult = repository.getAnimal(urlSuffix = "pig", someRequestValue = someValue)

        state.value = if (
            listOf(cowResult, sheepResult, pigResult).any { it is DataResult.Failure }
        ) {
            val errorMessage = listOf(cowResult, sheepResult, pigResult)
                .filter { it is DataResult.Failure }
                .joinToString { it.errorOrNull() ?: "" }
            State.FetchError(values = state.value.values, error = errorMessage)
        } else {
            State.Success(
                values = state.value.values,
                cowResult = cowResult.dataOrNull(),
                sheepResult = sheepResult.dataOrNull(),
                pigResult = pigResult.dataOrNull()
            )
        }
    }

    fun setIsReleaseMode(isRelease: Boolean) = viewModelScope.launch {
        setReleaseMode(isRelease)
        val newValues = state.value.values.copy(isRelease = isRelease)
        state.value = state.value.withValues(values = newValues)
    }

    fun setRequestText(request: String) {
        val newValues = state.value.values.copy(requestBody = request)
        state.value = state.value.withValues(values = newValues)
    }

    sealed class State {
        abstract val values: Values

        internal fun withValues(values: Values): State = when (this) {
            is Fetching -> copy(values = values)
            is FetchError -> copy(values = values)
            is Success -> copy(values = values)
        }

        /**
         * @property values
         */
        data class Fetching(
            override val values: Values = Values()
        ) : State()

        /**
         * @property values
         * @property error
         */
        data class FetchError(
            override val values: Values,
            val error: String
        ) : State()

        /**
         * @property values
         * @property cowResult
         * @property sheepResult
         * @property pigResult
         */
        data class Success(
            override val values: Values,
            val cowResult: AnimalDto? = null,
            val sheepResult: AnimalDto? = null,
            val pigResult: AnimalDto? = null,
        ) : State()

        /**
         * @property requestBody
         * @property isRelease
         */
        data class Values(
            val requestBody: String = "",
            val isRelease: Boolean = false
        )
    }
}
