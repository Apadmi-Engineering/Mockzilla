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
import com.apadmi.mockzilla.desktop.di.utils.getViewModel

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.scaffold.Widget
import com.apadmi.mockzilla.desktop.ui.scaffold.WidgetScaffold
import com.apadmi.mockzilla.desktop.ui.theme.AppTheme
import com.apadmi.mockzilla.desktop.ui.utils.androidStatusBarPadding
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionWidget
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidget
import com.apadmi.mockzilla.desktop.ui.widgets.misccontrols.MiscControlsWidget
import com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.MonitorLogsWidget
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

import org.koin.java.KoinJavaComponent

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun App(
    strings: Strings = LocalStrings.current
) = AppTheme {
    val viewModel = getViewModel<AppRootViewModel>()
    val state by viewModel.state.collectAsState()

    WidgetScaffold(
        modifier = Modifier.androidStatusBarPadding().fillMaxSize(),
        top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
        left = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
            listOf(
                Widget(strings.widgets.metaData.title) {
                    MetaDataWidget(connectedState.activeDevice.device)
                },
                Widget(strings.widgets.miscControls.title) {
                    MiscControlsWidget(connectedState.activeDevice.device)
                })
        } ?: emptyList(),
        right = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
            listOf(Widget(strings.widgets.endpointDetails.title) {
                Crossfade(
                    targetState = connectedState,
                    animationSpec = tween(durationMillis = 200)
                ) { newState ->
                    EndpointDetailsWidget(newState.activeDevice.device, newState.selectedEndpoint)
                }
            })
        } ?: emptyList(),
        middle = when (val newState = state) {
            is AppRootViewModel.State.Connected -> Widget {
                EndpointsWidget(
                    newState.activeDevice.device,
                    viewModel::setSelectedEndpoint
                )
            }
            AppRootViewModel.State.NewDeviceConnection -> Widget { DeviceConnectionWidget() }
            AppRootViewModel.State.UnsupportedDeviceMockzillaVersion -> Widget { UnsupportedDeviceMockzillaVersionWidget() }
        }.let { listOf(it) },
        bottom = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
            listOf(
                Widget(strings.widgets.logs.title) {
                    MonitorLogsWidget(connectedState.activeDevice.device)
                }
            )
        } ?: emptyList(),
    )
}
