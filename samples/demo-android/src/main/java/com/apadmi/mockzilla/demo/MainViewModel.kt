package com.apadmi.mockzilla.demo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository,
    private val setReleaseMode: (isRelease: Boolean) -> Unit,
) : ViewModel() {
    var state = mutableStateOf(State(request = "", cowResult = null, isRelease = false))
        private set

    fun makeRequest(someValue: String) {
        viewModelScope.launch {
            state.value = state.value.copy(cowResult = repository.getCow(someValue))
        }
    }

    fun setIsReleaseMode(isRelease: Boolean) {
        setReleaseMode(isRelease)
        state.value = state.value.copy(isRelease = isRelease)
    }

    fun setRequestText(request: String) {
        state.value = state.value.copy(request = request)
    }

    /**
     * @property request
     * @property cowResult
     * @property isRelease
     */
    data class State(
        val request: String,
        val cowResult: DataResult<CowDto, String>?,
        val isRelease: Boolean,
    )
}
