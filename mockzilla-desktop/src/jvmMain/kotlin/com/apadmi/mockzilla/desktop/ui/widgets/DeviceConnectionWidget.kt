package com.apadmi.mockzilla.desktop.ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.viewmodel.getViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel.State


@Composable
fun DeviceConnectionWidget() {
    val viewModel = remember { getViewModel<DeviceConnectionViewModel>() }
    val state by viewModel.state.collectAsState()

    DeviceConnectionContent(state)
}

@Composable
fun DeviceConnectionContent(state: State) {
    Text(state.toString())
}

@Composable
@Preview
fun DeviceConnectionWidgetPreview() = PreviewSurface {
    DeviceConnectionContent(State.Loading)
}