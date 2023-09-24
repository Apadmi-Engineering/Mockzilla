package com.apadmi.mockzilla.desktop.ui.widgets

import com.apadmi.mockzilla.desktop.ui.viewmodel.ViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel.State
import kotlinx.coroutines.flow.MutableStateFlow

class DeviceConnectionViewModel: ViewModel<State>() {

    override val state = MutableStateFlow<State>(State.Loading)
    sealed class State {
        object Loading: State()
    }

}