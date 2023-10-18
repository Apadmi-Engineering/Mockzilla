package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    Surface(modifier = modifier) {
        HorizontalTabList(
            tabs = state.devices.map { HorizontalTab(title = it.toString()) } + HorizontalTab(strings.widgets.deviceTabs.addDevice),
            selected = state.devices.indexOfFirst { it.isActive },
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
