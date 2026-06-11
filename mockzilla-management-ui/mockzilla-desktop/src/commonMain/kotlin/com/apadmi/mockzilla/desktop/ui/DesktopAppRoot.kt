package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
private const val globalControlsWidgetId = "global-controls"
private const val animationDuration = 300
private const val createPresetPanelWidth = 500
private const val scrimAlpha = 0.5f
private const val defaultLeftPanelWidth = 300
private const val defaultRightPanelWidth = 900
private const val globalControlsWidth = 400
private const val crossfadeDuration = 200
private val leftPanelWidth = defaultLeftPanelWidth.dp
private val rightPanelWidth = defaultRightPanelWidth.dp

@Composable
fun DesktopApp(
    strings: Strings = LocalStrings.current
) {
    AppTheme {
        val viewModel = getViewModel<AppRootViewModel>()
        val state by viewModel.state.collectAsState()

        var openWidgets by remember { mutableStateOf(setOf(devicePanelWidgetId)) }
        var logDetail by remember { mutableStateOf<LogEvent?>(null) }

        val onSelected: (String) -> Unit = { id ->
            val isExclusive = id == endpointDetailsWidgetId || id == logDetailsWidgetId
            openWidgets = if (openWidgets.contains(id)) {
                openWidgets.minus(id)
            } else {
                (if (isExclusive) {
                    openWidgets.minus(endpointDetailsWidgetId).minus(logDetailsWidgetId)
                } else {
                    openWidgets
                }).plus(id)
            }
        }

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
            onCloseLogDetail = {
                logDetail = null
                openWidgets = openWidgets.minus(logDetailsWidgetId)
            },
        )

        val connectedState = state as? AppRootViewModel.State.Connected
        val isPresetOpen = connectedState != null &&
                (createPresetWidgetId in openWidgets || editPresetWidgetId in openWidgets) &&
                connectedState.selectedEndpoint != null

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.mobileStatusBarPadding().fillMaxSize()) {
                // ── Top bar ──────────────────────────────────────────────────
                DeviceTabsWidget(
                    modifier = Modifier.fillMaxWidth(),
                )

                MiddleContentArea(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    openWidgets = openWidgets,
                    left = leftPanelWidgets(state),
                    right = rightWidgets,
                    middle = middleWidgets(
                        state,
                        onOpenGlobalControls = {
                            if (!openWidgets.contains(globalControlsWidgetId)) {
                                onSelected(globalControlsWidgetId)
                            }
                        },
                    ) {
                        viewModel.setSelectedEndpoint(it)
                        if (!openWidgets.contains(endpointDetailsWidgetId)) {
                            onSelected(endpointDetailsWidgetId)
                        }
                    },
                    onSelected = onSelected,
                    initialLeftPanelWidth = leftPanelWidth,
                    initialRightPanelWidth = rightPanelWidth,
                    isPresetOpen = isPresetOpen,
                    connectedState = connectedState,
                    creatingNewPreset = createPresetWidgetId in openWidgets,
                    onCancelPreset = {
                        openWidgets = openWidgets
                            .minus(editPresetWidgetId)
                            .minus(createPresetWidgetId)
                    },
                    globalControlsOpen = openWidgets.contains(globalControlsWidgetId),
                    onCloseGlobalControls = { onSelected(globalControlsWidgetId) },
                )

                // ── Bottom logs panel ─────────────────────────────────────────
                bottomPanelWidgets(
                    state = state,
                    onViewDetail = {
                        logDetail = it
                        if (!openWidgets.contains(logDetailsWidgetId)) {
                            onSelected(logDetailsWidgetId)
                        }
                    },
                    strings = strings,
                ).forEach { widget -> widget.ui() }
            }

            AnimatedErrorBanner(
                (state as? AppRootViewModel.State.Connected)?.error,
                viewModel::refreshAll,
                viewModel::dismissError
            )
        }
    }
}

/**
 * The middle content area: left/middle/right scaffold panels plus the
 * Create-Preset, Global-Controls, and scrim overlays.
 *
 * This lives in its own composable so that [ColumnScope] is **not** an ambient
 * implicit receiver, which would cause the compiler to resolve [AnimatedVisibility]
 * to [ColumnScope.AnimatedVisibility] and produce an "cannot be called in this
 * context" error.
 */
@Suppress("TOO_LONG_FUNCTION", "MAGIC_NUMBER")
@Composable
private fun MiddleContentArea(
    modifier: Modifier = Modifier,
    openWidgets: Set<String>,
    left: List<Widget>,
    middle: List<Widget>,
    right: List<Widget>,
    onSelected: (String) -> Unit,
    initialLeftPanelWidth: Dp,
    initialRightPanelWidth: Dp,
    isPresetOpen: Boolean,
    connectedState: AppRootViewModel.State.Connected?,
    creatingNewPreset: Boolean,
    onCancelPreset: () -> Unit,
    globalControlsOpen: Boolean,
    onCloseGlobalControls: () -> Unit,
) {
    Box(modifier = modifier) {
        WidgetScaffold(
            modifier = Modifier.fillMaxSize(),
            openWidgets = openWidgets,
            top = {},
            left = left,
            right = right,
            middle = middle,
            bottom = emptyList(),
            onSelected = onSelected,
            initialLeftPanelWidth = initialLeftPanelWidth,
            initialRightPanelWidth = initialRightPanelWidth,
        )

        // Scrim — dims everything when Create/Edit Preset is open
        AnimatedVisibility(
            visible = isPresetOpen,
            enter = fadeIn(animationSpec = tween(animationDuration)),
            exit = fadeOut(animationSpec = tween(animationDuration)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = scrimAlpha))
            )
        }

        // Create / Edit Preset overlay
        AnimatedVisibility(
            visible = isPresetOpen,
            enter = slideInHorizontally(animationSpec = tween(animationDuration)) { it },
            exit = slideOutHorizontally(animationSpec = tween(animationDuration)) { it },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            connectedState?.selectedEndpoint?.let { endpoint ->
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(createPresetPanelWidth.dp)
                        .shadow(8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    CreateEditPresetWidget(
                        device = connectedState.activeDevice.device,
                        activeEndpoint = endpoint,
                        creatingNewPreset = creatingNewPreset,
                        onCancel = onCancelPreset,
                    )
                }
            }
        }

        // Global Controls overlay
        AnimatedVisibility(
            visible = globalControlsOpen && connectedState != null,
            enter = slideInHorizontally(animationSpec = tween(animationDuration)) { it },
            exit = slideOutHorizontally(animationSpec = tween(animationDuration)) { it },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            connectedState?.let {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(globalControlsWidth.dp)
                        .shadow(8.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(topStart = 8.dp)
                        ),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 8.dp),
                ) {
                    GlobalControlsWidget(
                        device = connectedState.activeDevice.device,
                        onClose = onCloseGlobalControls,
                    )
                }
            }
        }
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
    onOpenGlobalControls: () -> Unit,
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit,
) = listOf(when (state) {
    is AppRootViewModel.State.Connected -> Widget(id = "endpoints") {
        EndpointsWidget(
            state.activeDevice.device,
            onEndpointClicked,
            onGlobalControlsClicked = onOpenGlobalControls
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
    logDetail: LogEvent?,
    strings: Strings,
    onCreatePreset: (EndpointConfiguration.Key) -> Unit,
    onEditPreset: (EndpointConfiguration.Key) -> Unit,
    onCloseLogDetail: () -> Unit,
) = (state as? AppRootViewModel.State.Connected)?.let { connectedState ->
    buildList {
        add(
            Widget(
                id = endpointDetailsWidgetId, title = strings.widgets.endpointDetails.title
            ) {
                Crossfade(
                    targetState = connectedState, animationSpec = tween(durationMillis = crossfadeDuration)
                ) { newState ->
                    EndpointDetailsWidget(
                        device = newState.activeDevice.device,
                        activeEndpoint = newState.selectedEndpoint,
                        onCreatePreset = onCreatePreset,
                        onEditPreset = onEditPreset,
                    )
                }
            }
        )
        add(
            Widget(
                id = logDetailsWidgetId, title = strings.widgets.logDetails.title
            ) {
                MonitorLogDetailsWidget(logDetail = logDetail, onClose = onCloseLogDetail)
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
    tint = MaterialTheme.colorScheme.onSurfaceVariant,
    contentDescription = LocalStrings.current.common.backDescription,
)
