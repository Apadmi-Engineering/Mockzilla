package com.apadmi.mockzilla.desktop.ui.devicetabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

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
    val tokens = LocalMockzillaTokens.current

    Column(modifier = modifier.background(tokens.bg1)) {
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

        HorizontalDivider(color = tokens.line1, thickness = 1.dp)
    }
}

@Composable
private fun DeviceChip(
    device: State.DeviceTabEntry,
    strings: Strings,
    onSelect: () -> Unit,
    onClose: () -> Unit,
) {
    val tokens = LocalMockzillaTokens.current
    val chipShape = RoundedCornerShape(8.dp)
    val dotColor = if (device.isConnected) tokens.ok else tokens.fg3
    val statusColor = if (device.isConnected) tokens.ok else tokens.fg3

    Box(
        modifier = Modifier
            .clip(chipShape)
            .background(if (device.isActive) tokens.bg3 else tokens.bg2)
            .border(
                width = 1.dp,
                color = if (device.isActive) tokens.line2 else tokens.line1,
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
                    color = if (device.isActive) tokens.fg0 else tokens.fg1,
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
                    tint = tokens.fg3,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Composable
private fun AddDeviceButton(label: String, onClick: () -> Unit) {
    val tokens = LocalMockzillaTokens.current

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
            tint = tokens.fg2,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = tokens.fg2,
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
