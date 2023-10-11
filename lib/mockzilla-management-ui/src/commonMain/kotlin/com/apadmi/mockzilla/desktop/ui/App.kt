package com.apadmi.mockzilla.desktop.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apadmi.mockzilla.desktop.ui.components.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.scaffold.Widget
import com.apadmi.mockzilla.desktop.ui.scaffold.WidgetScaffold
import com.apadmi.mockzilla.desktop.ui.theme.AppTheme
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionWidget

@Composable
fun App() = AppTheme {
    WidgetScaffold(
        modifier = Modifier.fillMaxSize(),
        top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
        left = listOf(Widget("Left 1") { Text("Left1") }),
        right = listOf(Widget("Right 1") { Text("Right1") }),
        middle = listOf(Widget("Middle 1") { DeviceConnectionWidget() }),
        bottom = listOf(Widget("Bottom 1") { Text("Bottom 1") }, Widget("Bottom 2", { Text("Bottom 2") })),
    )
}
