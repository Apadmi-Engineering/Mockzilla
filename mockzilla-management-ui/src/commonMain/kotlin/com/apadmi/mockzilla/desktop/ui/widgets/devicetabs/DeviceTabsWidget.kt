package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Pause
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
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel.*

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
        modifier = modifier,
    )
}

@Composable
fun DeviceTabsWidgetContent(
    state: State,
    modifier: Modifier = Modifier,
    strings: Strings = LocalStrings.current,
    onSelect: (State.DeviceTabEntry) -> Unit,
    onAddNewDevice: () -> Unit
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
                HorizontalTab(
                    title = device.name,
                    icon = if (device.isActive) {
                        if (device.isConnected) {
                            Icons.Filled.Edit
                        } else {
                            Icons.Filled.EditOff
                        }
                    } else {
                        Icons.Filled.Pause
                    },
                    subtitle = if (device.isConnected) {
                        strings.widgets.deviceTabs.connected
                    } else {
                        strings.widgets.deviceTabs.disconnected
                    }
                )
            } + HorizontalTab(
                title = strings.widgets.deviceTabs.addDevice,
                icon = Icons.Filled.Add,
                // always ensure tabs have a subtitle to keep tab heights the same
                subtitle = strings.widgets.deviceTabs.devices(state.devices.size),
            ),
            selected = selectedTab,
            onSelect = {
                if (it > state.devices.lastIndex) {
                    onAddNewDevice()
                } else {
                    onSelect(state.devices[it])
                }
            },
        )
    }
}
