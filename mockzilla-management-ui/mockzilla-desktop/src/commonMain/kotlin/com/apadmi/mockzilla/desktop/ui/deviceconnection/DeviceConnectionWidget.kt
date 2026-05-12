package com.apadmi.mockzilla.desktop.ui.deviceconnection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionViewModel.State
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
import org.jetbrains.compose.ui.tooling.preview.Preview

private fun DetectedDevice.State.toolTipText(strings: Strings) = when (this) {
    DetectedDevice.State.NotYourSimulator -> strings.widgets.deviceConnection.tooltips.notYourSimulator
    DetectedDevice.State.ReadyToConnect -> strings.widgets.deviceConnection.tooltips.readyToConnect
    DetectedDevice.State.Removed -> strings.widgets.deviceConnection.tooltips.removed
    DetectedDevice.State.Resolving -> strings.widgets.deviceConnection.tooltips.resolving
}

@Composable
private fun DetectedDevice.State.color() = when (this) {
    DetectedDevice.State.ReadyToConnect -> LocalMockzillaTokens.current.ok
    DetectedDevice.State.Removed,
    DetectedDevice.State.NotYourSimulator -> LocalMockzillaTokens.current.err

    DetectedDevice.State.Resolving -> LocalMockzillaTokens.current.fg2
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
        .background(LocalMockzillaTokens.current.bg0)
        .padding(horizontal = 32.dp),
    contentAlignment = Alignment.Center,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.92f),
        horizontalArrangement = Arrangement.spacedBy(96.dp),
        verticalAlignment = Alignment.CenterVertically,
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
            .widthIn(max = 620.dp)
    ) {

        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(LocalMockzillaTokens.current.bg1)
                .border(
                    width = 1.dp,
                    color = LocalMockzillaTokens.current.line1,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.MockzillaLogo,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = strings.widgets.deviceConnection.title,
            style = MaterialTheme.typography.headlineLarge,
            color = LocalMockzillaTokens.current.fg0,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = strings.widgets.deviceConnection.subTile,
            style = MaterialTheme.typography.bodyLarge,
            color = LocalMockzillaTokens.current.fg1
        )
        Spacer(modifier = Modifier.height(28.dp))

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
        modifier = Modifier.widthIn(min = 620.dp, max = 760.dp),
        shape = RoundedCornerShape(18.dp),
        color = tokens.bg1,
        border = BorderStroke(1.dp, tokens.line1),
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = "MANUAL CONNECTION",
                color = tokens.fg1,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                text = "Enter the IP address and port of the device running your app",
                color = tokens.fg1,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = LocalMonoFontFamily.current,
                    ),
                    value = state.ipAndPort,
                    onValueChange = onIpAndPortChanged,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Power,
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
                SolidButton(
                    modifier = Modifier.height(54.dp),
                    label = "Connect",
                    onClick = onConnect,
                )
            }
            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
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
        shape = RoundedCornerShape(10.dp),
        color = tokens.bg2,
        border = BorderStroke(1.dp, tokens.line1),
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 88.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().width(3.dp).background(statusColor),
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StandardTextTooltip(text = device.state.toolTipText(strings)) {
                    Surface(
                        modifier = Modifier.size(14.dp),
                        shape = CircleShape,
                        color = statusColor.copy(alpha = 0.16f),
                    ) {
                        Canvas(
                            modifier = Modifier.padding(3.5.dp),
                            onDraw = { drawCircle(color = statusColor) },
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = if (device.connectionName.length > 25) {
                            device.connectionName.take(22) + "..."
                        } else {
                            device.connectionName
                        },
                        color = tokens.fg0,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
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
                        leadingIcon = Icons.Outlined.Power,
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
        text = text,
        color = tokens.fg2,
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
            tint = LocalMockzillaTokens.current.accent,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalMockzillaTokens.current.fg1
        )
    }
}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//private fun DevicesList(
//    devices: List<DetectedDevice>,
//    onTapDevice: (DetectedDevice) -> Unit,
//    strings: Strings = LocalStrings.current,
//) = LazyColumn {
//    item {
//        Column(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Text(
//                text = strings.widgets.deviceConnection.autoConnectHeading,
//                style = MaterialTheme.typography.headlineLarge
//            )
//            Text(
//                text = strings.widgets.deviceConnection.autoConnectSubHeading,
//                style = MaterialTheme.typography.bodySmall
//            )
//            Spacer(Modifier.height(8.dp))
//        }
//    }
//
//    itemsIndexed(devices, key = { _, device -> device.connectionName }) { index, device ->
//        Row(
//            modifier = Modifier.animateItem().alternatingBackground(index).fillMaxWidth()
//                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            StandardTextTooltip(text = device.state.toolTipText(strings)) {
//                Canvas(
//                    modifier = Modifier.padding(end = 16.dp).size(12.dp),
//                    onDraw = { drawCircle(color = device.state.color()) })
//            }
//
//            Column(
//                verticalArrangement = Arrangement.spacedBy(4.dp),
//                modifier = Modifier.weight(1f).padding(end = 14.dp)
//            ) {
//                Text(device.prettyName(), maxLines = 1, overflow = TextOverflow.Ellipsis)
//                Text(
//                    modifier = Modifier.alpha(0.5f),
//                    text = buildString {
//                        device.metaData?.appName?.also {
//                            append(it)
//                            append(" | ")
//                        }
//                        append("${device.hostAddress}:${device.port}")
//                    },
//                    style = MaterialTheme.typography.bodySmall,
//                )
//            }
//
//            if (device.state == DetectedDevice.State.Resolving) {
//                CircularProgressIndicator(Modifier.padding(end = 8.dp).size(20.dp))
//            } else {
//                SolidButton(
//                    onClick = { onTapDevice(device) },
//                    label = strings.widgets.deviceConnection.autoConnectButton
//                )
//            }
//        }
//    }
//}

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