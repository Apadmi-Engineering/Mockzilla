package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
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
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.di.utils.evictDesktopViewModelsForKey
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.DeviceRootViewModel
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

import org.koin.core.parameter.parametersOf

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
private val leftPanelWidth = defaultLeftPanelWidth.dp
private val rightPanelWidth = defaultRightPanelWidth.dp

@Composable
fun DesktopApp(
    strings: Strings = LocalStrings.current
) {
    AppTheme {
        val activeDeviceMonitor = remember { MockzillaUiKoinContext.koin.get<ActiveDeviceMonitor>() }
        val selectedStatefulDevice by activeDeviceMonitor.selectedDevice.collectAsState()
        val selectedDevice = selectedStatefulDevice?.device
        val stateHolder = rememberSaveableStateHolder()

        // Evict per-device ViewModels when their device leaves allDevices entirely (not just
        // temporarily disconnected — the VM handles disconnection gracefully while preserving
        // selectedEndpoint). allDevices is a live collection, updated before the flow fires.
        LaunchedEffect(Unit) {
            var knownKeys = activeDeviceMonitor.allDevices.map { it.device.toString() }.toSet()
            activeDeviceMonitor.onDeviceConnectionStateChange.collect {
                val currentKeys = activeDeviceMonitor.allDevices.map { it.device.toString() }.toSet()
                (knownKeys - currentKeys).forEach { evictDesktopViewModelsForKey(it) }
                knownKeys = currentKeys
            }
        }

        Column(modifier = Modifier.mobileStatusBarPadding().fillMaxSize()) {
            DeviceTabsWidget(modifier = Modifier.fillMaxWidth())

            selectedDevice?.let {
                stateHolder.SaveableStateProvider(key = selectedDevice.toString()) {
                    DeviceContent(device = selectedDevice, strings = strings)
                }
            } ?: DeviceConnectionWidget()
        }
    }
}

@Suppress("TOO_LONG_FUNCTION")
@Composable
private fun DeviceContent(
    device: Device,
    strings: Strings
) {
    val viewModel = getViewModel<DeviceRootViewModel>(
        key = device.toString()
    ) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    when (val local = state) {
        DeviceRootViewModel.State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        is DeviceRootViewModel.State.Connected -> DeviceWidgetScaffoldContainer(local, strings, viewModel)
        else -> {
            // this is a generated else block
        }
    }
}

@Composable
private fun DeviceWidgetScaffoldContainer(
    connectedState: DeviceRootViewModel.State.Connected,
    strings: Strings,
    viewModel: DeviceRootViewModel
) {
    var openWidgets by rememberSaveable { mutableStateOf(setOf(devicePanelWidgetId)) }
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
        connectedState = connectedState,
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

    val isPresetOpen = (createPresetWidgetId in openWidgets || editPresetWidgetId in openWidgets) &&
            connectedState.selectedEndpoint != null

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MiddleContentArea(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                openWidgets = openWidgets,
                left = leftPanelWidgets(connectedState),
                right = rightWidgets,
                middle = middleWidgets(
                    connectedState,
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

            bottomPanelWidgets(
                connectedState = connectedState,
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
            connectedState.error,
            viewModel::refreshAll,
            viewModel::dismissError
        )
    }
}

/**
 * The middle content area: left/middle/right scaffold panels plus the
 * Create-Preset, Global-Controls, and scrim overlays.
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
    connectedState: DeviceRootViewModel.State.Connected,
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
            connectedState.selectedEndpoint?.let { endpoint ->
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
            visible = globalControlsOpen,
            enter = slideInHorizontally(animationSpec = tween(animationDuration)) { it },
            exit = slideOutHorizontally(animationSpec = tween(animationDuration)) { it },
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
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

@Suppress("LAMBDA_IS_NOT_LAST_PARAMETER")
private fun bottomPanelWidgets(
    connectedState: DeviceRootViewModel.State.Connected,
    onViewDetail: (LogEvent) -> Unit,
    strings: Strings
) = listOf(
    Widget(id = "monitor-logs", strings.widgets.logs.title) {
        MonitorLogsWidget(
            device = connectedState.activeDevice.device,
            onViewDetail = onViewDetail
        )
    }
)

private fun middleWidgets(
    connectedState: DeviceRootViewModel.State.Connected,
    onOpenGlobalControls: () -> Unit,
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit,
) = listOf(
    Widget(id = "endpoints") {
        EndpointsWidget(
            connectedState.activeDevice.device,
            onEndpointClicked,
            onGlobalControlsClicked = onOpenGlobalControls
        )
    }
)

private fun rightPanelWidgets(
    connectedState: DeviceRootViewModel.State.Connected,
    logDetail: LogEvent?,
    strings: Strings,
    onCreatePreset: (EndpointConfiguration.Key) -> Unit,
    onEditPreset: (EndpointConfiguration.Key) -> Unit,
    onCloseLogDetail: () -> Unit,
) = buildList {
    add(
        Widget(
            id = endpointDetailsWidgetId, title = strings.widgets.endpointDetails.title
        ) {
            EndpointDetailsWidget(
                device = connectedState.activeDevice.device,
                activeEndpoint = connectedState.selectedEndpoint,
                onCreatePreset = onCreatePreset,
                onEditPreset = onEditPreset,
            )
        }
    )
    add(
        Widget(
            id = logDetailsWidgetId, title = strings.widgets.logDetails.title
        ) {
            MonitorLogDetailsWidget(
                device = connectedState.activeDevice.device,
                logDetail = logDetail,
                onClose = onCloseLogDetail,
            )
        }
    )
}

private fun leftPanelWidgets(
    connectedState: DeviceRootViewModel.State.Connected,
) = listOf(
    Widget(id = devicePanelWidgetId) {
        Column {
            MetaDataWidget(connectedState.activeDevice.device)
            MiscControlsWidget(connectedState.activeDevice.device)
        }
    }
)

@Composable
private fun CloseButtonIcon() = Icon(
    imageVector = Icons.Default.Close,
    tint = MaterialTheme.colorScheme.onSurfaceVariant,
    contentDescription = LocalStrings.current.common.backDescription,
)
