package com.apadmi.mockzilla.desktop.ui.devicetabs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel.*
import com.apadmi.mockzilla.desktop.ui.utils.desktopTertiaryPointerClick
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.scaffold.HorizontalTab
import com.apadmi.mockzilla.ui.ui.common.scaffold.HorizontalTabList
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlin.collections.plus

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
    val selectedDevice = state.devices.indexOfFirst { it.isActive }
    val selectedTab = if (selectedDevice == -1) {
        state.devices.lastIndex + 1
    } else {
        selectedDevice
    }

    HorizontalTabList(
        modifier = modifier.background(tokens.bg1),
        tabs = state.devices.map { device ->
            HorizontalTab(
                title = device.name,
                leadingIcon = null,
                leadingContent = {
                    val dotColor = if (device.isConnected) tokens.ok else tokens.fg3
                    Canvas(modifier = Modifier.size(6.dp)) {
                        drawCircle(color = dotColor)
                    }
                },
                subtitle = if (device.isConnected) {
                    strings.widgets.deviceTabs.connected
                } else {
                    strings.widgets.deviceTabs.disconnected
                },
                trailing = {
                    IconButton(
                        content = {
                            Icon(
                                Icons.Filled.Close,
                                strings.widgets.deviceTabs.closeButtonDescription,
                                tint = tokens.fg2,
                            )
                        },
                        modifier = Modifier.size(24.dp),
                        onClick = { onCloseTab(device) },
                    )
                },
                modifier = Modifier.desktopTertiaryPointerClick(
                    onClick = { onCloseTab(device) },
                ),
            )
        } + HorizontalTab(
            title = strings.widgets.deviceTabs.addDevice,
            leadingIcon = Icons.Filled.Add,
            subtitle = strings.widgets.deviceTabs.devices(state.devices.size),
        ),
        selected = selectedTab,
        onSelect = {
            if (it == selectedTab) {
                return@HorizontalTabList
            }
            if (it > state.devices.lastIndex) {
                onAddNewDevice()
            } else {
                onSelect(state.devices[it])
            }
        },
    )
}

@Preview
@Composable
private fun DeviceTabsWidgetPreview() = PreviewSurface {
    DeviceTabsWidgetContent(
        state = State(
            devices = listOf(
                State.DeviceTabEntry(
                    name = "Pixel 8 Pro",
                    isActive = true,
                    isConnected = true,
                    underlyingDevice = Device(ip = "", port = ""),
                ),
                State.DeviceTabEntry(
                    name = "iPhone 15",
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
