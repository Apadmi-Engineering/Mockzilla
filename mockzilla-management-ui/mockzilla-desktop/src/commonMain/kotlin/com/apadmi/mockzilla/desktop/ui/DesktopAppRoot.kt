package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.utils.Platform

import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionWidget
import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.scaffold.Widget
import com.apadmi.mockzilla.desktop.ui.scaffold.WidgetScaffold
import com.apadmi.mockzilla.desktop.ui.utils.mobileStatusBarPadding
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.components.AnimatedErrorBanner
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.misccontrols.MiscControlsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.MonitorLogsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsWidget
import kotlin.collections.buildList
import kotlin.let

private const val devicePanelWidgetId = "device-panel"
private const val endpointDetailsWidgetId = "endpoint-details"
private const val logDetailsWidgetId = "log-details"
private const val editPresetWidgetId = "edit-preset"
private const val createPresetWidgetId = "create-preset"

@Composable
fun DesktopApp(
    strings: Strings = LocalStrings.current
) {
    AppTheme {
        val viewModel = getViewModel<AppRootViewModel>()
        val state by viewModel.state.collectAsState()

        var openWidgets by remember { mutableStateOf(setOf(devicePanelWidgetId)) }
        var logDetail by remember { mutableStateOf<LogEvent?>(null) }

        val rightWidgets = rightPanelWidgets(
            state = state,
            logDetail = logDetail,
            strings = strings,
            onCreatePreset = {
                viewModel.setSelectedEndpoint(it)
                openWidgets = openWidgets.minus(editPresetWidgetId)
                openWidgets = openWidgets.plus(createPresetWidgetId)
            },
            onEditPreset = {
                viewModel.setSelectedEndpoint(it)
                openWidgets = openWidgets.minus(createPresetWidgetId)
                openWidgets = openWidgets.plus(editPresetWidgetId)
            },
        )

        WidgetScaffold(
            modifier = Modifier.mobileStatusBarPadding().fillMaxSize(),
            openWidgets = openWidgets,
            top = {
                DeviceTabsWidget(
                    modifier = Modifier.fillMaxWidth()
                )
            },
            left = leftPanelWidgets(state),
            right = rightWidgets,
            middle = middleWidgets(
                state, openWidgets, onCloseEditor = {
                    openWidgets = openWidgets
                        .minus(editPresetWidgetId)
                        .minus(createPresetWidgetId)
                }
            ) {
                viewModel.setSelectedEndpoint(it)
                openWidgets = openWidgets.plus(endpointDetailsWidgetId)
            },
            bottom = bottomPanelWidgets(
                state = state,
                onViewDetail = {
                    logDetail = it
                    openWidgets = openWidgets.plus(logDetailsWidgetId)
                },
                strings = strings,
            ),
            onSelected = {
                openWidgets = if (openWidgets.contains(it)) {
                    openWidgets.minus(it)
                } else {
                    openWidgets.plus(it)
                }
            }
        )

        AnimatedErrorBanner(
            (state as? AppRootViewModel.State.Connected)?.error,
            viewModel::refreshAll,
            viewModel::dismissError
        )
    }
}

@Suppress("LAMBDA_IS_NOT_LAST_PARAMETER")
private fun bottomPanelWidgets(
    state: AppRootViewModel.State,
    onViewDetail: (LogEvent) -> Unit,
    strings: Strings
) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    listOf(
        Widget(id = "monitor-logs", strings.widgets.logs.title) {
            MonitorLogsWidget(
                device = connectedState.activeDevice.device,
                onViewDetail = onViewDetail
            )
        }
    )
} ?: emptyList()

@Suppress("diktat") // Diktat generates an invalid else block for some reason
private fun middleWidgets(
    state: AppRootViewModel.State,
    openWidgets: Set<String>,
    onCloseEditor: () -> Unit,
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit,
) = listOf(when (state) {
    is AppRootViewModel.State.Connected -> Widget(id = "endpoints") {
        val isGlobalControlsOpen = remember { mutableStateOf(false) }
        val selectedEndpoint = state.selectedEndpoint
        when {
            (createPresetWidgetId in openWidgets || editPresetWidgetId in openWidgets)
                    && selectedEndpoint != null -> Column {
                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onCloseEditor,
                ) { CloseButtonIcon() }
                CreateEditPresetWidget(
                    device = state.activeDevice.device,
                    activeEndpoint = selectedEndpoint,
                    creatingNewPreset = createPresetWidgetId in openWidgets
                )
            }
            isGlobalControlsOpen.value -> Column {
                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        isGlobalControlsOpen.value = false
                    }) { CloseButtonIcon() }
                GlobalControlsWidget(state.activeDevice.device)
            }

            else -> EndpointsWidget(
                state.activeDevice.device,
                onEndpointClicked,
                { isGlobalControlsOpen.value = true }
            )
        }
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
    logDetail: LogEvent?,
    strings: Strings,
    onCreatePreset: (EndpointConfiguration.Key) -> Unit,
    onEditPreset: (EndpointConfiguration.Key) -> Unit

) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    buildList {
        add(
            Widget(
                id = endpointDetailsWidgetId, title = strings.widgets.endpointDetails.title
            ) {
                Crossfade(
                    targetState = connectedState, animationSpec = tween(durationMillis = 200)
                ) { newState ->
                    EndpointDetailsWidget(
                        device = newState.activeDevice.device,
                        activeEndpoint = newState.selectedEndpoint,
                        onCreatePreset = onCreatePreset,
                        onEditPreset = onEditPreset
                    )
                }
            }
        )
        add(
            Widget(
                id = logDetailsWidgetId, title = strings.widgets.logDetails.title
            ) {
                MonitorLogDetailsWidget(logDetail)
            }
        )
    }
} ?: emptyList()

private fun leftPanelWidgets(
    state: AppRootViewModel.State,
) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    listOf(
        Widget(id = devicePanelWidgetId) {
            Column {
                MetaDataWidget(connectedState.activeDevice.device)
                MiscControlsWidget(connectedState.activeDevice.device)
            }
        }
    )
} ?: emptyList()

@Composable
private fun CloseButtonIcon() = Icon(
    imageVector = Icons.Default.Close,
    tint = LocalMockzillaTokens.current.fg1,
    contentDescription = LocalStrings.current.common.backDescription,
)
