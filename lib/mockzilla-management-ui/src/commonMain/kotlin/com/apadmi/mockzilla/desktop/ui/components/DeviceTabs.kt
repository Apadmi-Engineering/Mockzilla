package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList

import cafe.adriel.lyricist.LocalStrings

@Composable
fun DeviceTabsWidget(
    modifier: Modifier,
    strings: Strings = LocalStrings.current
) {
    var selected by remember { mutableStateOf<Int?>(0) }
    DeviceTabsWidgetContent(
        devices = listOf(strings.widgets.deviceTabs.addDevice),
        selected = selected,
        onSelect = { selected = it },
        modifier = modifier,
    )
}

@Composable
fun DeviceTabsWidgetContent(
    devices: List<String>,
    selected: Int?,
    modifier: Modifier = Modifier,
    onSelect: (Int?) -> Unit,
) {
    Surface(modifier = modifier) {
        HorizontalTabList(
            tabs = devices.map { HorizontalTab(title = it) },
            selected = selected,
            onSelect = onSelect,
        )
    }
}
