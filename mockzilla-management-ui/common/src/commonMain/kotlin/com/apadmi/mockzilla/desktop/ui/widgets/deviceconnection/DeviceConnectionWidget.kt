package com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.engine.adb.AdbConnection
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionViewModel.State

import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Composable
fun DeviceConnectionWidget() {
    val viewModel = getViewModel<DeviceConnectionViewModel>()
    val state by viewModel.state.collectAsState()

    DeviceConnectionContent(state, viewModel::onIpAndPortChanged, viewModel::connect8080To0)
}

@Composable
fun DeviceConnectionContent(
    state: State,
    onIpAndPortChanged: (String) -> Unit,
    onTapAdbDevice: (AdbConnection) -> Unit
) = Column {
    Text("State: ${state.connectionState}")
    TextField(value = state.ipAndPort, onValueChange = onIpAndPortChanged)
    state.adbDevices.forEach {
        Button(onClick = { onTapAdbDevice(it) }, enabled = it.isActive) {
            Text("Connect to ${it.name} (isActive: ${it.isActive})")
        }
    }
}

@ShowkaseComposable("DeviceConnection-Idle", "DeviceConnection")
@Composable
@Preview
fun DeviceConnectionWidgetPreview() = PreviewSurface {
    DeviceConnectionContent(State(), {}) {}
}
