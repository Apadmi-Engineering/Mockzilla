package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.wrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.wrapper.MiddlePaneWrapperViewModel.*

@Composable
fun MiddlePaneWrapperWidget(
    endpointsWidget: @Composable () -> Unit,
    deviceConnectionWidget: @Composable () -> Unit
) {
    val viewModel = getViewModel<MiddlePaneWrapperViewModel>()
    val state by viewModel.state.collectAsState()

    when (state) {
        State.Endpoints -> endpointsWidget()
        State.NewDeviceConnection -> deviceConnectionWidget()
    }
}
