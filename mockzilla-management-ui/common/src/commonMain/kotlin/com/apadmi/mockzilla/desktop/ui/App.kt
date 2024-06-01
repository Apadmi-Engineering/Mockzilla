@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice

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
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidget
import com.apadmi.mockzilla.desktop.ui.widgets.misccontrols.MiscControlsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.MonitorLogsWidget
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.java.KoinJavaComponent

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
    val scope = rememberCoroutineScope()
    var appState by remember { mutableStateOf(AppState(activeEndpoint = null)) }
    var activeDevice by remember { mutableStateOf<StatefulDevice?>(null) }

    KoinJavaComponent.getKoin().get<ActiveDeviceMonitor>().selectedDevice.onEach {
        activeDevice = it
    }.launchIn(scope)

    WidgetScaffold(
        modifier = Modifier.androidStatusBarPadding().fillMaxSize(),
        top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
        left = listOf(
            Widget(strings.widgets.metaData.title) {
                activeDevice?.let {
                    MetaDataWidget(it.device)
                } ?: run {
                    Text("Empty metadata")
                }
            },
            Widget(strings.widgets.miscControls.title) {
                MiscControlsWidget(activeDevice?.device)
            }
        ),
        right = listOf(Widget(strings.widgets.endpointDetails.title) {
            Crossfade(
                targetState = appState.activeEndpoint,
                animationSpec = tween(durationMillis = 200)
            ) { endpoint ->
                activeDevice?.let {
                    EndpointDetailsWidget(it.device, endpoint)
                } ?: run {
                    Text("Empty top level")
                }
            }
        }),
        middle = listOf(Widget {
            activeDevice?.let { statefulDevice ->
                EndpointsWidget(statefulDevice.device) {
                    appState = appState.copy(activeEndpoint = it)
                }
            } ?: run {
                DeviceConnectionWidget()
            }
        }),
        bottom = listOf(
            Widget(strings.widgets.logs.title) {
                activeDevice?.let { statefulDevice ->
                    MonitorLogsWidget(statefulDevice.device)
                } ?: run {
                    Text("Empty logs")
                }
            }
        ),
    )
}
