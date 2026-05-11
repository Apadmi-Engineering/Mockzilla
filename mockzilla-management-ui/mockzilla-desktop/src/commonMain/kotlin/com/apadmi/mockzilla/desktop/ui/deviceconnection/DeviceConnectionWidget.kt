package com.apadmi.mockzilla.desktop.ui.deviceconnection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionViewModel.*

import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.connection.DetectedDevice
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StandardTextTooltip
import com.apadmi.mockzilla.ui.ui.common.components.buttons.SolidButton
import com.apadmi.mockzilla.ui.ui.common.theme.alternatingBackground
import com.apadmi.mockzilla.ui.utils.Platform
import org.jetbrains.compose.ui.tooling.preview.Preview

private fun DetectedDevice.State.toolTipText(strings: Strings) = when (this) {
    DetectedDevice.State.NotYourSimulator -> strings.widgets.deviceConnection.tooltips.notYourSimulator
    DetectedDevice.State.ReadyToConnect -> strings.widgets.deviceConnection.tooltips.readyToConnect
    DetectedDevice.State.Removed -> strings.widgets.deviceConnection.tooltips.removed
    DetectedDevice.State.Resolving -> strings.widgets.deviceConnection.tooltips.resolving
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

    DeviceConnectionContent(
        state,
        viewModel::onIpAndPortChanged,
        viewModel::connectToDevice
    )
}

@Composable
fun DeviceConnectionContent(
    state: State,
    onIpAndPortChanged: (String) -> Unit,
    onTapDevice: (DetectedDevice) -> Unit,
    strings: Strings = LocalStrings.current,
) = Box(modifier = Modifier.padding(200.dp,0.dp,0.dp,0.dp), contentAlignment = Alignment.CenterStart) {
    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .padding(top = 100.dp)
    ) {

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.MockzillaLogo,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = strings.widgets.deviceConnection.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = strings.widgets.deviceConnection.subTile,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(5.dp))

        BulletItem(
            icon = Icons.Default.Bolt,
            text = strings.widgets.deviceConnection.bullet1
        )

        BulletItem(
            icon = Icons.Default.DragIndicator,
            text = strings.widgets.deviceConnection.bullet2
        )

        BulletItem(
            icon = Icons.Default.AccessTime,
            text = strings.widgets.deviceConnection.bullet3
        )

        BulletItem(
            icon = Icons.Default.Menu,
            text = strings.widgets.deviceConnection.bullet4
        )
            }
        }

@Composable
private fun BulletItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF00ACC1),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun DeviceConnectionWidgetPreview() = PreviewSurface {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        DeviceConnectionContent(
            state = State(
                ipAndPort = "127.0.0.1:60000",
                connectionState = State.ConnectionState.Connecting,
                devices = listOf(
                    DetectedDevice(
                        connectionName = "iPhone",
                        metaData = null,
                        hostAddress = "127.0.0.1",
                        hostAddresses = listOf(),
                        port = 60000,
                        adbConnection = null,
                        state = DetectedDevice.State.ReadyToConnect
                    )
                ),
            ),
            onIpAndPortChanged = {},
            onTapDevice = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DevicesList(
    devices: List<DetectedDevice>,
    onTapDevice: (DetectedDevice) -> Unit,
    strings: Strings = LocalStrings.current,
) = LazyColumn {
    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = strings.widgets.deviceConnection.autoConnectHeading,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = strings.widgets.deviceConnection.autoConnectSubHeading,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
        }
    }

    itemsIndexed(devices, key = { _, device -> device.connectionName }) { index, device ->
        Row(
            modifier = Modifier.animateItem().alternatingBackground(index).fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StandardTextTooltip(text = device.state.toolTipText(strings)) {
                Canvas(
                    modifier = Modifier.padding(end = 16.dp).size(12.dp),
                    onDraw = { drawCircle(color = device.state.color()) })
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(device.prettyName(), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = buildString {
                        device.metaData?.appName?.also {
                            append(it)
                            append(" | ")
                        }
                        append("${device.hostAddress}:${device.port}")
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            if (device.state == DetectedDevice.State.Resolving) {
                CircularProgressIndicator(Modifier.padding(end = 8.dp).size(20.dp))
            } else {
                SolidButton(
                    onClick = { onTapDevice(device) },
                    label = strings.widgets.deviceConnection.autoConnectButton
                )
            }
        }
    }
}
