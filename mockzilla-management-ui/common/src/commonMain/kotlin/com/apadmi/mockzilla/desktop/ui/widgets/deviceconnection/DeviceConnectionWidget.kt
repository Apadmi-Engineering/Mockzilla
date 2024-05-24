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
import com.apadmi.mockzilla.desktop.engine.connection.DetectedDevice
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionViewModel.State

import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Composable
fun DeviceConnectionWidget() {
    val viewModel = getViewModel<DeviceConnectionViewModel>()
    val state by viewModel.state.collectAsState()

    DeviceConnectionContent(state, viewModel::onIpAndPortChanged, viewModel::connectToDevice)
}

@Composable
fun DeviceConnectionContent(
    state: State,
    onIpAndPortChanged: (String) -> Unit,
    onTapDevice: (DetectedDevice) -> Unit
) = Column {
    Text("State: ${state.connectionState}")
    TextField(value = state.ipAndPort, onValueChange = onIpAndPortChanged)
    Button(onClick = { onIpAndPortChanged("127.0.0.1:8080") }) {
        Text("Set to localhost:8080")
    }
    state.devices.forEach { device ->
        Button(onClick = { onTapDevice(device) }) {
            device.metaData?.let {
                Text("${device.state} ${device.connectionName} ${it.deviceModel}: ${device.hostAddress}")
            } ?: Text("${device.state} ${device.connectionName} ${device.hostAddress}:${device.port}")
        }
    }
}

@ShowkaseComposable("DeviceConnection-Idle", "DeviceConnection")
@Composable
@Preview
fun DeviceConnectionWidgetPreview() = PreviewSurface {
    DeviceConnectionContent(State(), {}) {}
}
