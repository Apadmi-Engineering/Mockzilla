package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel.State

@Composable
fun DeviceTabsWidget(
    modifier: Modifier
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
    val selectedDevice = state.devices.indexOfFirst { it.isActive }
    val selectedTab = if (selectedDevice == -1) {
        state.devices.lastIndex + 1
    } else {
        selectedDevice
    }

    Surface(modifier = modifier) {
        HorizontalTabList(
            tabs = state.devices.map { device ->
                deviceTab(device, strings, onCloseTab)
            } + HorizontalTab(
                title = strings.widgets.deviceTabs.addDevice,
                leadingIcon = Icons.Filled.Add,
                // always ensure tabs have a subtitle to keep tab heights the same
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
}

internal expect fun deviceTab(
    device: State.DeviceTabEntry,
    strings: Strings,
    onCloseTab: (State.DeviceTabEntry) -> Unit
): HorizontalTab
