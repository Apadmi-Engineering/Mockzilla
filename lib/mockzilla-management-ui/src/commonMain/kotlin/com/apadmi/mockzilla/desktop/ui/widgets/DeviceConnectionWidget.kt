package com.apadmi.mockzilla.desktop.ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import com.apadmi.mockzilla.desktop.di.getViewModel
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel.State

import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Composable
fun DeviceConnectionWidget() {
    val viewModel = getViewModel<DeviceConnectionViewModel>()
    val state by viewModel.state.collectAsState()

    DeviceConnectionContent(state, viewModel::onIpAndPortChanged)
}

@Composable
fun DeviceConnectionContent(state: State, onIpAndPortChanged: (String) -> Unit) = Column {
    Text("State: ${state.connectionState}")
    TextField(value = state.ipAndPort, onValueChange = onIpAndPortChanged)
}

@ShowkaseComposable("DeviceConnection-Idle", "DeviceConnection")
@Composable
@Preview
fun deviceConnectionWidgetPreview() = PreviewSurface {
    DeviceConnectionContent(State()) {}
}
