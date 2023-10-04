package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList

@Composable
fun DeviceTabsWidget(
    modifier: Modifier
) {
    var selected by remember { mutableStateOf<Int?>(0) }
    DeviceTabsWidgetContent(
        devices = listOf("Device 1", "Device 2", "+ Add Device"),
        selected = selected,
        onSelect = { selected = it },
        modifier = modifier,
    )
}

@Composable
fun DeviceTabsWidgetContent(
    devices: List<String>,
    selected: Int?,
    onSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        HorizontalTabList(
            tabs = devices.map { HorizontalTab(title = it) },
            selected = selected,
            onSelect = onSelect,
        )
    }
}