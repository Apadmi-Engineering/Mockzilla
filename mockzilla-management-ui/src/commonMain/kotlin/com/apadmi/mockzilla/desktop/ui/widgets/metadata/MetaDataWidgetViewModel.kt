package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import androidx.compose.runtime.Immutable
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.models.MetaData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MetaDataWidgetViewModel(
    private val device: Device,
    private val metaDataUseCase: MetaDataUseCase,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)

    init {
        viewModelScope.launch { reloadData() }
    }

    private suspend fun reloadData() {
        state.value = getMetaData()
    }

    private suspend fun getMetaData() = metaDataUseCase.getMetaData(device).fold(onSuccess = {
        State.DisplayMetaData(it)
    }, onFailure = {
        State.Error
    })

    @Immutable
    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class DisplayMetaData(val metaData: MetaData) : State()
    }
}
