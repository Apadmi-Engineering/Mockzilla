package com.apadmi.mockzilla.desktop.ui.deviceconnection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionViewModel.*
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget

import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.connection.DetectedDevice
import com.apadmi.mockzilla.ui.engine.connection.IpAddress
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StandardTextTooltip
import com.apadmi.mockzilla.ui.ui.common.components.buttons.SolidButton
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.alternatingBackground
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
) = Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 48.dp),
    contentAlignment = Alignment.Center,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 1100.dp),
        horizontalArrangement = Arrangement.spacedBy(96.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
                contentAlignment = Alignment.TopEnd,
        ) {
            ProductIntro(strings = strings)
        }

        Box(
            modifier = Modifier
                .weight(1f),
                contentAlignment = Alignment.TopStart,
        ) {
            ConnectionCard(
                state = state,
                onIpAndPortChanged = onIpAndPortChanged,
                onConnect = { onIpAndPortChanged(state.ipAndPort) },
                onTapDevice = onTapDevice,
                strings = strings,
            )
        }
    }
}

@Composable
private fun ProductIntro(strings: Strings) {
    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
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
            color = MaterialTheme.colorScheme.onBackground,
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
private fun ConnectionCard(
    state: State,
    onIpAndPortChanged: (String) -> Unit,
    onConnect: () -> Unit,
    onTapDevice: (DetectedDevice) -> Unit,
    strings: Strings,
) {
    val tokens = LocalMockzillaTokens.current
    Surface(
        modifier = Modifier.widthIn(min = 520.dp, max = 560.dp),
        shape = RoundedCornerShape(8.dp),
        color = tokens.bg1,
        border = BorderStroke(1.dp, tokens.line1),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "MANUAL CONNECTION",
                color = tokens.fg0,
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                text = "Enter the IP address and port of the device running your app",
                color = tokens.fg0,
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = state.ipAndPort,
                    onValueChange = onIpAndPortChanged,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.ElectricalServices,
                            contentDescription = null,
                            tint = tokens.fg2,
                        )
                    },
                    placeholder = { Text(text = strings.widgets.deviceConnection.ipAndPort, fontFamily = FontFamily.Monospace) },
                    colors = OutlinedTextFieldDefaults.colors(

                        // text
                        focusedTextColor = tokens.fg0,
                        unfocusedTextColor = tokens.fg0,

                        // container background
                        focusedContainerColor = tokens.bg2,
                        unfocusedContainerColor = tokens.bg2,

                        // borders
                        focusedBorderColor = tokens.line2,
                        unfocusedBorderColor = tokens.line1,

                        // placeholder
                        focusedPlaceholderColor = tokens.fg3,
                        unfocusedPlaceholderColor = tokens.fg3,

                        // cursor
                        cursorColor = tokens.accent,

                        // icons
                        focusedLeadingIconColor = tokens.fg2,
                        unfocusedLeadingIconColor = tokens.fg2,
                    )
                )
                SolidButton(label = "Connect", onClick = onConnect)
            }
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(Modifier.weight(1f), color = tokens.line1)
                Text(
                    text = "or connect automatically",
                    color = tokens.fg2,
                    style = MaterialTheme.typography.labelMedium,
                )
                HorizontalDivider(Modifier.weight(1f), color = tokens.line1)
            }
            DiscoveredDevicesSection(
                devices = state.devices,
                onTapDevice = onTapDevice,
                strings = strings,
            )
        }
    }
}

@Composable
private fun DiscoveredDevicesSection(
    devices: List<DetectedDevice>,
    onTapDevice: (DetectedDevice) -> Unit,
    strings: Strings,
) {
    val tokens = LocalMockzillaTokens.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "DISCOVERED ON NETWORK",
                color = tokens.fg1,
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(Modifier.width(8.dp))
            Canvas(Modifier.size(6.dp)) {
                drawCircle(tokens.ok)
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = "scanning...",
                color = tokens.fg2,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = LocalMonoFontFamily.current,
            )
        }
        LazyColumn(
            modifier = Modifier.heightIn(max = 220.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(devices, key = { _, device -> device.connectionName }) { _, device ->
                DiscoveredDeviceRow(
                    device = device,
                    onTapDevice = onTapDevice,
                    strings = strings,
                )
            }
        }
    }
}

@Composable
private fun DiscoveredDeviceRow(
    device: DetectedDevice,
    onTapDevice: (DetectedDevice) -> Unit,
    strings: Strings,
) {
    val tokens = LocalMockzillaTokens.current
    val statusColor = device.state.color()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        color = tokens.bg2,
        border = BorderStroke(1.dp, tokens.line1),
    ) {
        Row(
            modifier = Modifier
                .height(74.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().width(2.dp).background(statusColor),
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp, end = 18.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StandardTextTooltip(text = device.state.toolTipText(strings)) {
                    Surface(
                        modifier = Modifier.padding(end = 14.dp).size(16.dp),
                        shape = CircleShape,
                        color = statusColor.copy(alpha = 0.16f),
                    ) {
                        Canvas(
                            modifier = Modifier.padding(4.dp),
                            onDraw = { drawCircle(color = statusColor) },
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.weight(1f).padding(end = 10.dp),
                ) {
                    Text(
                        text = device.connectionName,
                        color = tokens.fg0,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    DeviceMetaLine(
                        device.metaData?.let { "${it.appName} · ${it.appPackage}" }
                            ?: "${device.hostAddress}:${device.port}",
                    )
                    device.metaData?.also {
                        DeviceMetaLine("${it.operatingSystemVersion} · ${it.deviceModel}")
                    }
                }

                if (device.state == DetectedDevice.State.Resolving) {
                    CircularProgressIndicator(Modifier.size(20.dp))
                } else {
                    SolidButton(
                        modifier = Modifier.width(132.dp).height(40.dp),
                        onClick = { onTapDevice(device) },
                        leadingIcon = Icons.Outlined.ElectricalServices,
                        label = strings.widgets.deviceConnection.autoConnectButton,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceMetaLine(text: String) {
    val tokens = LocalMockzillaTokens.current
    Text(
        modifier = Modifier.alpha(0.72f),
        text = text,
        color = tokens.fg1,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = LocalMonoFontFamily.current,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
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

@Preview(
    name = "Medium Tablet",
    widthDp = 1280,
    heightDp = 800,
)
@Composable
private fun DeviceConnectionWidgetMediumTabletPreview() = PreviewSurface {
    DeviceConnectionContent(
        state = State(
            ipAndPort = "127.0.0.1:8080",
            connectionState = State.ConnectionState.Disconnected,
            devices = listOf(
                DetectedDevice(
                    connectionName = "iosSimulator-iPhone 16 Plus",
                    metaData = MetaData(
                        appName = "Runner",
                        appPackage = "sus.Internal",
                        operatingSystemVersion = "iOS Version",
                        deviceModel = "iPhone 16 Plus",
                        appVersion = "1.0.0",
                        runTarget = RunTarget.IosSimulator,
                        mockzillaVersion = "3.0.0-alpha2",
                    ),
                    hostAddress = "127.0.0.1",
                    hostAddresses = listOf(IpAddress("127.0.0.1")),
                    port = 8080,
                    adbConnection = null,
                    state = DetectedDevice.State.ReadyToConnect,
                ),
                DetectedDevice(
                    connectionName = "Pixel 8 Pro",
                    metaData = MetaData(
                        appName = "Runner",
                        appPackage = "sus.Internal",
                        operatingSystemVersion = "Android 14",
                        deviceModel = "Pixel 8 Pro",
                        appVersion = "1.0.0",
                        runTarget = RunTarget.AndroidDevice,
                        mockzillaVersion = "3.0.0-alpha2",
                    ),
                    hostAddress = "192.168.1.42",
                    hostAddresses = listOf(IpAddress("192.168.1.42")),
                    port = 8080,
                    adbConnection = null,
                    state = DetectedDevice.State.ReadyToConnect,
                ),
            ),
        ),
        onIpAndPortChanged = {},
        onTapDevice = {},
    )
}