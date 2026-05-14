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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel.State
import com.apadmi.mockzilla.desktop.ui.utils.desktopTertiaryPointerClick
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeviceTabsWidget(
    modifier: Modifier,
) {
    val viewModel = getViewModel<DeviceTabsViewModel>()
    val state by viewModel.state.collectAsState()

    DeviceTabsWidgetContent(
        state = state,
        onSelect = viewModel::onChangeDevice,
        onAddNewDevice = viewModel::addNewDevice,
        onCloseTab = viewModel::removeDevice,
        modifier = modifier,
    )
}

@Composable
fun DeviceTabsWidgetContent(
    state: State,
    modifier: Modifier = Modifier,
    strings: Strings = LocalStrings.current,
    onSelect: (State.DeviceTabEntry) -> Unit,
    onAddNewDevice: () -> Unit,
    onCloseTab: (State.DeviceTabEntry) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier.background(colorScheme.surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            state.devices.forEach { device ->
                DeviceChip(
                    device = device,
                    strings = strings,
                    onSelect = { onSelect(device) },
                    onClose = { onCloseTab(device) },
                )
            }

            AddDeviceButton(
                label = strings.widgets.deviceTabs.addDevice,
                onClick = onAddNewDevice,
            )
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
    val statusColor = if (device.isConnected) colorScheme.success.primary else colorScheme.onSurfaceFaint

    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(if (device.isActive) colorScheme.surfaceVariant else colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = if (device.isActive) colorScheme.outlineVariant else colorScheme.outline,
                shape = chipShape
            )
            .clickable(onClick = onSelect)
            .desktopTertiaryPointerClick(onClick = onClose)
            .padding(start = 10.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)
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
                    .background(dotColor.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (device.isActive) colorScheme.onSurface else colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (device.isConnected) {
                        strings.widgets.deviceTabs.connected
                    } else {
                        strings.widgets.deviceTabs.disconnected
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
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
            tint = colorScheme.onSurfaceMuted,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = colorScheme.onSurfaceMuted,
        )
    }
}

@Preview
@Composable
private fun DeviceTabsWidgetPreviewLight() = PreviewSurface() {
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
