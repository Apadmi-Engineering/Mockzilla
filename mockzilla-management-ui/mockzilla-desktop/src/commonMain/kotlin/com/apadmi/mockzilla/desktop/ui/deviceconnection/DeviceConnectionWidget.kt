package com.apadmi.mockzilla.desktop.ui.deviceconnection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.surfaceSubtle
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val compactLayoutBreakpointDp = 1100
private const val compactMaxWidthDp = 720

private fun DetectedDevice.State.toolTipText(strings: Strings) = when (this) {
    DetectedDevice.State.NotYourSimulator -> strings.widgets.deviceConnection.tooltips.notYourSimulator
    DetectedDevice.State.ReadyToConnect -> strings.widgets.deviceConnection.tooltips.readyToConnect
    DetectedDevice.State.Removed -> strings.widgets.deviceConnection.tooltips.removed
    DetectedDevice.State.Resolving -> strings.widgets.deviceConnection.tooltips.resolving
}

@Composable
private fun DetectedDevice.State.color() = when (this) {
    DetectedDevice.State.ReadyToConnect -> MaterialTheme.colorScheme.success.primary
    DetectedDevice.State.Removed,
    DetectedDevice.State.NotYourSimulator -> MaterialTheme.colorScheme.error

    DetectedDevice.State.Resolving -> MaterialTheme.colorScheme.onSurfaceMuted
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
        .background(MaterialTheme.colorScheme.background),
    contentAlignment = Alignment.Center,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isCompact = maxWidth < compactLayoutBreakpointDp.dp

        if (isCompact) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val compactWidth = compactMaxWidthDp.dp

                Box(
                    modifier = Modifier
                        .widthIn(max = compactWidth),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    ProductIntro(strings = strings)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .widthIn(max = compactWidth),
                    contentAlignment = Alignment.CenterStart,
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
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    ProductIntro(strings = strings)
                }

                Spacer(modifier = Modifier.width(100.dp))

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
    }
}

@Composable
private fun ProductIntro(strings: Strings) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .widthIn(max = 620.dp)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = colorScheme.outline,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.MockzillaLogo,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = strings.widgets.deviceConnection.title,
            style = MaterialTheme.typography.headlineLarge,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = strings.widgets.deviceConnection.subTile,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

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
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colorScheme.surfaceSubtle,
        border = BorderStroke(1.dp, colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = strings.widgets.deviceConnection.networkConnection,
                color = colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                text = strings.widgets.deviceConnection.promptToEnterIp,
                color = colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp)
                        .border(
                            width = 0.5.dp,
                            color = colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),

                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = LocalMonoFontFamily.current,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                    ),
                    value = state.ipAndPort,
                    onValueChange = onIpAndPortChanged,
                    singleLine = true,

                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Power,
                            contentDescription = null,
                            tint = colorScheme.onSurfaceMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    },

                    placeholder = {
                        Text(
                            text = strings.widgets.deviceConnection.ipAndPort,
                            fontFamily = LocalMonoFontFamily.current,
                            fontSize = 14.sp,
                            lineHeight = 14.sp
                        )
                    },

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,

                        focusedContainerColor = colorScheme.surfaceVariant,
                        unfocusedContainerColor = colorScheme.surfaceVariant,

                        focusedBorderColor = colorScheme.outlineVariant,
                        unfocusedBorderColor = colorScheme.outline,

                        focusedPlaceholderColor = colorScheme.onSurfaceFaint,
                        unfocusedPlaceholderColor = colorScheme.onSurfaceFaint,

                        cursorColor = colorScheme.primary,

                        focusedLeadingIconColor = colorScheme.onSurfaceMuted,
                        unfocusedLeadingIconColor = colorScheme.onSurfaceMuted,
                    )
                )
                SolidButton(
                    modifier = Modifier.height(32.dp)
                        .align(Alignment.CenterVertically),
                    label = strings.widgets.deviceConnection.connect,
                    onClick = onConnect,
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                )
            }
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(Modifier.weight(1f), color = colorScheme.outline)
                Text(
                    text = strings.widgets.deviceConnection.connectAutomatically,
                    color = colorScheme.onSurfaceMuted,
                    style = MaterialTheme.typography.labelMedium,
                )
                HorizontalDivider(Modifier.weight(1f), color = colorScheme.outline)
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
    val colorScheme = MaterialTheme.colorScheme
    val successColor = colorScheme.success.primary
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = strings.widgets.deviceConnection.discoveredNetwork,
                color = colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(Modifier.width(8.dp))
            Canvas(Modifier.size(8.dp)) {
                drawCircle(successColor)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = strings.widgets.deviceConnection.scanning,
                color = colorScheme.onSurfaceMuted,
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
    val colorScheme = MaterialTheme.colorScheme
    val statusColor = device.state.color()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, colorScheme.outline),
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 88.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .height(88.dp)
                    .width(4.dp)
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    ),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StandardTextTooltip(text = device.state.toolTipText(strings)) {
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = CircleShape,
                        color = statusColor.copy(alpha = 0.16f),
                    ) {
                        Canvas(
                            modifier = Modifier.padding(3.5.dp),
                            onDraw = { drawCircle(color = statusColor) },
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = if (device.connectionName.length > 25) {
                            device.connectionName.take(22) + strings.widgets.deviceConnection.dot
                        } else {
                            device.connectionName
                        },
                        color = colorScheme.onSurface,
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

                Spacer(modifier = Modifier.width(32.dp))

                if (device.state == DetectedDevice.State.Resolving) {
                    CircularProgressIndicator(Modifier.size(32.dp))
                } else {
                    SolidButton(
                        modifier = Modifier.width(132.dp).height(32.dp),
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
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceMuted,
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
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
