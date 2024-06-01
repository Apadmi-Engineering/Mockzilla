@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.scaffold.Widget
import com.apadmi.mockzilla.desktop.ui.scaffold.WidgetScaffold
import com.apadmi.mockzilla.desktop.ui.theme.AppTheme
import com.apadmi.mockzilla.desktop.ui.utils.androidStatusBarPadding
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionWidget
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.wrapper.MiddlePaneWrapperWidget
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidget
import com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.MonitorLogsWidget

@Composable
fun App(
    strings: Strings = LocalStrings.current
) = AppTheme {
    WidgetScaffold(
        modifier = Modifier.androidStatusBarPadding().fillMaxSize(),
        top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
        left = listOf(Widget(strings.widgets.metaData.title) { MetaDataWidget() }),
        right = listOf(Widget("Right 1") { EndpointDetailsWidget() }),
        middle = listOf(Widget("") {
            MiddlePaneWrapperWidget({ EndpointsWidget() },
                { DeviceConnectionWidget() }
            )
        }),
        bottom = listOf(
            Widget(strings.widgets.logs.title) { MonitorLogsWidget() }
        ),
    )
}
