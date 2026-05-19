package com.apadmi.mockzilla.desktop.ui.devicetabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel.State
import com.apadmi.mockzilla.desktop.ui.utils.desktopTertiaryPointerClick
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.success

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeviceTabsWidget(
    modifier: Modifier,
    isGlobalControlsOpen: Boolean = false,
    onGlobalControlsClick: () -> Unit = {},
) {
    val viewModel = getViewModel<DeviceTabsViewModel>()
    val state by viewModel.state.collectAsState()

    DeviceTabsWidgetContent(
        state = state,
        onSelect = viewModel::onChangeDevice,
        onAddNewDevice = viewModel::addNewDevice,
        onCloseTab = viewModel::removeDevice,
        isGlobalControlsOpen = isGlobalControlsOpen,
        onGlobalControlsClick = onGlobalControlsClick,
        modifier = modifier,
    )
}

@Composable
fun DeviceTabsWidgetContent(
    state: State,
    modifier: Modifier = Modifier,
    isGlobalControlsOpen: Boolean = false,
    strings: Strings = LocalStrings.current,
    onSelect: (State.DeviceTabEntry) -> Unit,
    onAddNewDevice: () -> Unit,
    onCloseTab: (State.DeviceTabEntry) -> Unit,
    onGlobalControlsClick: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier.background(colorScheme.surfaceContainer)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (state.devices.isEmpty()) {
                    Text(
                        text = strings.widgets.deviceTabs.devices(0),
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                } else {
                    state.devices.forEach { device ->
                        DeviceChip(
                            device = device,
                            strings = strings,
                            onSelect = { onSelect(device) },
                            onClose = { onCloseTab(device) },
                        )
                    }
                }

                if (state.devices.isNotEmpty()) {
                    AddDeviceButton(
                        label = strings.widgets.deviceTabs.addDevice,
                        onClick = onAddNewDevice,
                    )
                }
            }

            if (state.devices.any { it.isActive }) {
                GlobalControlsButton(
                    label = strings.widgets.globalControls.title,
                    isOpen = isGlobalControlsOpen,
                    onClick = onGlobalControlsClick
                )
            }
        }

        HorizontalDivider(color = colorScheme.outline, thickness = 1.dp)
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun DeviceChip(
    device: State.DeviceTabEntry,
    strings: Strings,
    onSelect: () -> Unit,
    onClose: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val chipShape = RoundedCornerShape(8.dp)
    val dotColor = if (device.isConnected) colorScheme.success.primary else colorScheme.onSurfaceFaint

    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(if (device.isActive) colorScheme.surface else Color.Transparent)
            .then(
                if (device.isActive) {
                    Modifier.border(
                        width = 1.dp,
                        color = colorScheme.outline,
                        shape = chipShape
                    )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onSelect)
            .desktopTertiaryPointerClick(onClick = onClose)
            .padding(start = 10.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 11.sp),
                    color = if (device.isActive) colorScheme.onSurface else colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (device.isConnected) {
                        strings.widgets.deviceTabs.connected
                    } else {
                        strings.widgets.deviceTabs.disconnected
                    },
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                    color = colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = strings.widgets.deviceTabs.closeButtonDescription,
                    tint = colorScheme.onSurfaceFaint,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Composable
private fun AddDeviceButton(label: String, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = colorScheme.onSurfaceVariant,
        )
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun GlobalControlsButton(
    label: String,
    isOpen: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier = Modifier
            .clip(shape)
            .background(if (isOpen) colorScheme.primary else colorScheme.surface)
            .border(1.dp, if (isOpen) colorScheme.primary else colorScheme.outline, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Tune,
            contentDescription = null,
            tint = if (isOpen) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isOpen) colorScheme.onPrimary else colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun DeviceTabsWidgetPreviewLight() = PreviewSurface {
    DeviceTabsWidgetContent(
        state = State(
            devices = listOf(
                State.DeviceTabEntry(
                    name = "iosSimulator-iPhone 16 Plus",
                    isActive = true,
                    isConnected = true,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
                State.DeviceTabEntry(
                    name = "Pixel 8 Pro",
                    isActive = false,
                    isConnected = false,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
            ),
        ),
        onSelect = {},
        onAddNewDevice = {},
        onCloseTab = {},
    )
}

@Preview
@Composable
private fun DeviceTabsWidgetPreviewDark() = PreviewSurface(darkTheme = true) {
    DeviceTabsWidgetContent(
        state = State(
            devices = listOf(
                State.DeviceTabEntry(
                    name = "iosSimulator-iPhone 16 Plus",
                    isActive = true,
                    isConnected = true,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
                State.DeviceTabEntry(
                    name = "Pixel 8 Pro",
                    isActive = false,
                    isConnected = false,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
            ),
        ),
        onSelect = {},
        onAddNewDevice = {},
        onCloseTab = {},
    )
}
