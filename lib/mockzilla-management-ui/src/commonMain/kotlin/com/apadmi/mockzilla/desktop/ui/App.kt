@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.lyricist.LocalStrings
import com.apadmi.mockzilla.desktop.ui.components.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.scaffold.Widget
import com.apadmi.mockzilla.desktop.ui.scaffold.WidgetScaffold
import com.apadmi.mockzilla.desktop.ui.theme.AppTheme
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionWidget

import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidget

@ShowkaseRoot
class RootShowkaseModule : ShowkaseRootModule

@Composable
fun App(
    strings: Strings = LocalStrings.current
) = AppTheme {

    WidgetScaffold(
        modifier = Modifier.fillMaxSize(),
        top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
        left = listOf(Widget(strings.widgets.metaData.title) { MetaDataWidget() }),
        right = listOf(Widget("Right 1") { Text("Right1") }),
        middle = listOf(Widget("Middle 1") { DeviceConnectionWidget() }),
        bottom = listOf(Widget("Bottom 1") { Text("Bottom 1") }, Widget("Bottom 2", { Text("Bottom 2") })),
    )
}
