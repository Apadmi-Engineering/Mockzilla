@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule

@ShowkaseRoot
class RootShowkaseModule : ShowkaseRootModule

/**
 * @property activeEndpoint
 */
private data class AppState(
    val activeEndpoint: EndpointConfiguration.Key?
)

@Composable
fun App(
    strings: Strings = LocalStrings.current
) = AppTheme {
    var appState by remember { mutableStateOf(AppState(activeEndpoint = null)) }

    WidgetScaffold(
        modifier = Modifier.androidStatusBarPadding().fillMaxSize(),
        top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
        left = listOf(Widget(strings.widgets.metaData.title) { MetaDataWidget() }),
        right = listOf(Widget(strings.widgets.endpointDetails.title) {
            Crossfade(
                targetState = appState.activeEndpoint,
                animationSpec = tween(durationMillis = 200)) { endpoint ->
                EndpointDetailsWidget(endpoint)
            }
        }),
        middle = listOf(Widget("") {
            MiddlePaneWrapperWidget({
                EndpointsWidget {
                    appState = appState.copy(activeEndpoint = it)
                }
            },
                { DeviceConnectionWidget() }
            )
        }),
        bottom = listOf(
            Widget(strings.widgets.logs.title) { MonitorLogsWidget() }
        ),
    )
}
