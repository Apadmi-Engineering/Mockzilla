@file:Suppress("diktat") // For some reason Diktat fails to parse this file ¯\_(ツ)_/¯

package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.apadmi.mockzilla.desktop.di.utils.getViewModel

import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.AppRootViewModel.State.Connected.ErrorBannerState
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

private const val endpointDetailsWidgetId = "endpoint-details"

@Composable
fun App(
    strings: Strings = LocalStrings.current
) {
    AppTheme {
        val viewModel = getViewModel<AppRootViewModel>()
        val state by viewModel.state.collectAsState()

        var openWidgets by remember { mutableStateOf(emptySet<String>()) }

        WidgetScaffold(
            modifier = Modifier.androidStatusBarPadding().fillMaxSize(),
            openWidgets = openWidgets,
            top = { DeviceTabsWidget(modifier = Modifier.fillMaxWidth()) },
            left = leftPanelWidgets(state, strings),
            right = rightPanelWidgets(state, strings),
            middle = middleWidgets(state) {
                viewModel.setSelectedEndpoint(it)
                openWidgets = openWidgets.plus(endpointDetailsWidgetId)
            },
            bottom = bottomPanelWidgets(state, strings),
            onSelected = {
                openWidgets = if (openWidgets.contains(it)) {
                    openWidgets.minus(it)
                } else {
                    openWidgets.plus(it)
                }
            }
        )

        (state as? AppRootViewModel.State.Connected)?.error?.let {
            ErrorBanner(it, viewModel::refreshAll)
        }
    }
}

@Composable
private fun ErrorBanner(
    state: ErrorBannerState,
    onRefreshAll: () -> Unit
) = Box(modifier = Modifier.fillMaxWidth().background(state.backgroundColor())) {
    Row(Modifier.align(Alignment.Center)) {
        Text("Oops", textAlign = TextAlign.Center)
        if (state == ErrorBannerState.UnknownError) {
            Button(onClick = onRefreshAll) {
                Text("Refresh")
            }
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun ErrorBannerState.backgroundColor() = when (this) {
    ErrorBannerState.ConnectionLost -> Color(0XFFB58C1B)
    ErrorBannerState.UnknownError -> MaterialTheme.colorScheme.errorContainer
}

private fun bottomPanelWidgets(
    state: AppRootViewModel.State,
    strings: Strings
) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    listOf(
        Widget(id = "monitor-logs", strings.widgets.logs.title) {
            MonitorLogsWidget(connectedState.activeDevice.device)
        }
    )
} ?: emptyList()

private fun middleWidgets(
    state: AppRootViewModel.State,
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit
) = listOf(when (state) {
    is AppRootViewModel.State.Connected -> Widget(id = "endpoints") {
        EndpointsWidget(
            state.activeDevice.device,
            onEndpointClicked
        )
    }

    AppRootViewModel.State.NewDeviceConnection -> Widget(id = "device-connection") {
        DeviceConnectionWidget()
    }

    AppRootViewModel.State.UnsupportedDeviceMockzillaVersion -> Widget(id = "unsupported-mockzilla") {
        UnsupportedDeviceMockzillaVersionWidget()
    }
})

private fun rightPanelWidgets(
    state: AppRootViewModel.State,
    strings: Strings
) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    listOf(Widget(id = endpointDetailsWidgetId, strings.widgets.endpointDetails.title) {
        Crossfade(
            targetState = connectedState,
            animationSpec = tween(durationMillis = 200)
        ) { newState ->
            EndpointDetailsWidget(newState.activeDevice.device, newState.selectedEndpoint)
        }
    })
} ?: emptyList()

private fun leftPanelWidgets(
    state: AppRootViewModel.State,
    strings: Strings
) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    listOf(
        Widget(id = "meta-data", strings.widgets.metaData.title) {
            MetaDataWidget(connectedState.activeDevice.device)
        },
        Widget(id = "misc-controls", strings.widgets.miscControls.title) {
            MiscControlsWidget(connectedState.activeDevice.device)
        })
} ?: emptyList()
