package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel.State

internal actual fun deviceTab(
    device: State.DeviceTabEntry,
    strings: Strings,
    onCloseTab: (State.DeviceTabEntry) -> Unit
) = HorizontalTab(
    title = device.name,
    leadingIcon = if (device.isActive) {
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
    },
    trailing = {
        IconButton(content = {
            Icon(Icons.Filled.Close, strings.widgets.deviceTabs.closeButtonDescription)
        }, modifier = Modifier.size(24.dp), onClick = { onCloseTab(device) })
    }
)
