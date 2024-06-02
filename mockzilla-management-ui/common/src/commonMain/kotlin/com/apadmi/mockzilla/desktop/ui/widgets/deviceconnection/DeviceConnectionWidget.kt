package com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection

import androidx.compose.animation.AnimatedContent
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.engine.connection.DetectedDevice
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.components.ShowkaseComposable
import com.apadmi.mockzilla.desktop.ui.components.StandardTextTooltip
import com.apadmi.mockzilla.desktop.ui.theme.alternatingBackground
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionViewModel.State
import com.apadmi.mockzilla.desktop.utils.Platform

import com.apadmi.common.generated.resources.Res
import com.apadmi.common.generated.resources.mockzilla_logo
import org.jetbrains.compose.resources.painterResource

private fun DetectedDevice.State.toolTipText() = when (this) {
    DetectedDevice.State.NotYourSimulator -> "We don't think this is your simulator, but you can try to connect! (Probably won't work)"
    DetectedDevice.State.ReadyToConnect -> ""
    DetectedDevice.State.Removed -> "This device seems to have disconnected"
    DetectedDevice.State.Resolving -> "We're still for this device to come online"
}

private fun DetectedDevice.State.color() = when (this) {
    DetectedDevice.State.ReadyToConnect -> Color.Green
    DetectedDevice.State.Removed,
    DetectedDevice.State.NotYourSimulator -> Color.Red

    DetectedDevice.State.Resolving -> Color.Gray
}

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
    onTapDevice: (DetectedDevice) -> Unit,
    strings: Strings = LocalStrings.current,
) = Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
                .height(100.dp),
            painter = painterResource(Res.drawable.mockzilla_logo),
            contentDescription = null
        )

        Text(
            text = "Enter IP and port to connect to a device",
            style = MaterialTheme.typography.headlineLarge
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.ipAndPort,
            onValueChange = onIpAndPortChanged,
            label = { Text(strings.widgets.deviceConnection.ipInputLabel) }
        )
        Spacer(Modifier.height(4.dp))
        AnimatedContent(
            targetState = state.hasDevices
        ) {
            if (it) {
                DevicesList(
                    devices = state.devices,
                    onIpAndPortChanged = onIpAndPortChanged,
                    onTapDevice = onTapDevice
                )
            }
        }
    }
}

@ShowkaseComposable("DeviceConnection-Idle", "DeviceConnection")
@Composable
@Preview
fun DeviceConnectionWidgetPreview() = PreviewSurface {
    DeviceConnectionContent(State(), {}, {})
}

@Composable
private fun DevicesList(
    devices: List<DetectedDevice>,
    onIpAndPortChanged: (String) -> Unit,
    onTapDevice: (DetectedDevice) -> Unit
) = LazyColumn {
    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Or..",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Choose a device to connect automatically",
                style = MaterialTheme.typography.bodySmall
            )
            if (Platform.current == Platform.Android) {
                Button(onClick = { onIpAndPortChanged("127.0.0.1:8080") }) {
                    Text("Set to localhost:8080")
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    itemsIndexed(devices) { index, device ->
        Row(
            modifier = Modifier.alternatingBackground(index).fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StandardTextTooltip(text = device.state.toolTipText()) {
                Canvas(
                    modifier = Modifier.padding(end = 16.dp).size(12.dp),
                    onDraw = { drawCircle(color = device.state.color()) })
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(device.prettyName())
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = "${device.hostAddress}:${device.port}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.weight(1f))

            if (device.state == DetectedDevice.State.Resolving) {
                CircularProgressIndicator(Modifier.padding(end = 8.dp).size(20.dp))
            } else {
                Button(onClick = { onTapDevice(device) }) {
                    Text("Connect")
                }
            }
        }
    }
}
