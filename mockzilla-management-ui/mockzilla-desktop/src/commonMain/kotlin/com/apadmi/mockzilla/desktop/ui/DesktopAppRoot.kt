package com.apadmi.mockzilla.desktop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min

import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionWidget
import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalDraggableDivider
import com.apadmi.mockzilla.desktop.ui.scaffold.VerticalDraggableDivider
import com.apadmi.mockzilla.desktop.ui.utils.isLinux
import com.apadmi.mockzilla.desktop.ui.utils.mobileStatusBarPadding
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.internal.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.internal.di.utils.evictDesktopViewModelsForKey
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.ui.common.DeviceRootViewModel
import com.apadmi.mockzilla.ui.ui.common.components.AnimatedErrorBanner
import com.apadmi.mockzilla.ui.ui.common.components.LinuxUnsupportedBanner
import com.apadmi.mockzilla.ui.ui.common.components.PlatformVerticalScrollbar
import com.apadmi.mockzilla.ui.ui.common.scaffold.VerticalTab
import com.apadmi.mockzilla.ui.ui.common.scaffold.VerticalTabList
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

private const val animationDuration = 300
private const val rightPanelEnterDurationMs = 160
private const val rightPanelEnterFadeDurationMs = 120
private const val rightPanelExitDurationMs = 130
private const val rightPanelExitFadeDurationMs = 100
private const val scrimAlpha = 0.5f
private const val leftPanelMinWidthDp = 200
private const val rightPanelMinWidthDp = 350
private const val presetPanelMinWidthDp = 400
private const val globalControlsPanelMinWidthDp = 300
private const val centerMinWidthDp = 300
private const val logsMinHeightDp = 100

// Dp cannot be stored directly by rememberSaveable, so we round-trip through a list of primitives.
// If LayoutState fields change, update both branches of this saver accordingly.
@Suppress("MAGIC_NUMBER")
private val layoutStateSaver = Saver<LayoutState, List<Any?>>(
    save = { saverScope ->
        listOf(
            saverScope.leftWidthDp,
            saverScope.rightWidthDp,
            saverScope.logsExpandedHeightDp,
            saverScope.presetWidthDp,
            saverScope.globalControlsWidthDp,
            saverScope.rightPanelTab?.raw,
            saverScope.presetOpen,
            saverScope.creatingNewPreset,
            saverScope.globalControlsOpen,
            saverScope.logsExpanded,
        )
    },
    restore = { list ->
        LayoutState(
            leftWidthDp = list[0] as Float,
            rightWidthDp = list[1] as Float,
            logsExpandedHeightDp = list[2] as Float,
            presetWidthDp = list[3] as Float,
            globalControlsWidthDp = list[4] as Float,
            rightPanelTab = RightPanelTab.from(list[5] as? String),
            presetOpen = list[6] as Boolean,
            creatingNewPreset = list[7] as Boolean,
            globalControlsOpen = list[8] as Boolean,
            logsExpanded = list[9] as Boolean,
        )
    }
)

/**
 * @property raw
 */
private enum class RightPanelTab(val raw: String) {
    EndpointDetails("EndpointDetails"),
    LogDetails("LogDetails"),
    ;

    companion object {
        fun from(raw: String?) = entries.firstOrNull { raw == it.raw }
    }
}

/**
 * @property leftWidthDp
 * @property rightWidthDp
 * @property logsExpandedHeightDp
 * @property presetWidthDp
 * @property globalControlsWidthDp
 * @property rightPanelTab
 * @property presetOpen
 * @property creatingNewPreset
 * @property globalControlsOpen
 * @property logsExpanded
 */
private data class LayoutState(
    val leftWidthDp: Float = 260f,
    val rightWidthDp: Float = 500f,
    val logsExpandedHeightDp: Float = 220f,
    val presetWidthDp: Float = 500f,
    val globalControlsWidthDp: Float = 400f,
    val rightPanelTab: RightPanelTab? = null,
    val presetOpen: Boolean = false,
    val creatingNewPreset: Boolean = true,
    val globalControlsOpen: Boolean = false,
    val logsExpanded: Boolean = false,
)

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
            activeDeviceMonitor.onDeviceRemoved.collect {
                evictDesktopViewModelsForKey(it)
            }
        }

        Column(modifier = Modifier.mobileStatusBarPadding().fillMaxSize()) {
            DeviceTabsWidget(modifier = Modifier.fillMaxWidth())

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                selectedDevice?.let {
                    stateHolder.SaveableStateProvider(key = selectedDevice.toString()) {
                        DeviceContent(device = selectedDevice, strings = strings)
                    }
                } ?: DeviceConnectionWidget()
            }

            if (isLinux() && selectedDevice == null) {
                LinuxUnsupportedBanner()
            }
        }
    }
}

@Composable
private fun ScrimOverlay(visible: Boolean, onDismiss: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(animationDuration)),
            exit = fadeOut(animationSpec = tween(animationDuration)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = scrimAlpha))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDismiss,
                    )
            )
        }
    }
}

@Composable
private fun DeviceContent(
    device: Device,
    strings: Strings
) {
    val viewModel = getViewModel<DeviceRootViewModel>(
        device = device
    ) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    when (val local = state) {
        DeviceRootViewModel.State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        is DeviceRootViewModel.State.Connected -> ConnectedDeviceLayout(local, strings, viewModel)
        else -> {
            // this is a generated else block
        }
    }
}

@Suppress(
    "TOO_LONG_FUNCTION",
    "LOCAL_VARIABLE_EARLY_DECLARATION",
    "MAGIC_NUMBER"
)
@Composable
private fun ConnectedDeviceLayout(
    connectedState: DeviceRootViewModel.State.Connected,
    strings: Strings,
    viewModel: DeviceRootViewModel,
) {
    val density = LocalDensity.current
    var totalWidth by remember { mutableStateOf(0.dp) }

    var state by rememberSaveable(stateSaver = layoutStateSaver) { mutableStateOf(LayoutState()) }
    var logDetail by remember { mutableStateOf<LogEvent?>(null) }

    // In-flight drag widths: updated every frame during a drag, not persisted — they equal the
    // settled state at rest and are snapped back to it when the drag stops.
    var leftDragWidth by remember { mutableStateOf(state.leftWidthDp.dp) }
    var rightDragWidth by remember { mutableStateOf(state.rightWidthDp.dp) }
    var logsDragHeight by remember { mutableStateOf(state.logsExpandedHeightDp.dp) }
    var presetDragWidth by remember { mutableStateOf(state.presetWidthDp.dp) }
    var globalControlsDragWidth by remember { mutableStateOf(state.globalControlsWidthDp.dp) }

    val clampLeft = { width: Dp ->
        if (totalWidth > 0.dp) {
            val remaining = max(leftPanelMinWidthDp.dp, totalWidth - centerMinWidthDp.dp - max(state.rightWidthDp.dp, rightPanelMinWidthDp.dp))
            min(max(leftPanelMinWidthDp.dp, width), remaining)
        } else {
            max(leftPanelMinWidthDp.dp, width)
        }
    }
    val clampRight = { width: Dp ->
        if (totalWidth > 0.dp) {
            val remaining = max(rightPanelMinWidthDp.dp, totalWidth - centerMinWidthDp.dp - max(state.leftWidthDp.dp, leftPanelMinWidthDp.dp))
            min(max(rightPanelMinWidthDp.dp, width), remaining)
        } else {
            max(rightPanelMinWidthDp.dp, width)
        }
    }

    val presetVisible = state.presetOpen && connectedState.selectedEndpoint != null

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onSizeChanged { size -> totalWidth = with(density) { size.width.toDp() } }
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Left panel: always visible, resizable
                    LeftTab(state, connectedState)
                    HorizontalDraggableDivider(
                        onDrag = { offset ->
                            leftDragWidth += with(density) { offset.toDp() }
                            state = state.copy(leftWidthDp = clampLeft(leftDragWidth).value)
                        },
                        onDragStopped = { leftDragWidth = state.leftWidthDp.dp },
                    )

                    // Center: fills remaining space
                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        EndpointsWidget(
                            device = connectedState.activeDevice.device,
                            onEndpointClicked = { key ->
                                viewModel.setSelectedEndpoint(key)
                                state = state.copy(rightPanelTab = RightPanelTab.EndpointDetails.takeUnless { key == null })
                            },
                            onGlobalControlsClicked = { state = state.copy(globalControlsOpen = true) },
                        )
                    }

                    // Right panel: collapsible, resizable
                    AnimatedVisibility(
                        visible = state.rightPanelTab != null,
                        enter = expandHorizontally(
                            expandFrom = Alignment.End,
                            animationSpec = tween(rightPanelEnterDurationMs),
                        ) + fadeIn(animationSpec = tween(rightPanelEnterFadeDurationMs)),
                        exit = shrinkHorizontally(
                            shrinkTowards = Alignment.End,
                            animationSpec = tween(rightPanelExitDurationMs),
                        ) + fadeOut(animationSpec = tween(rightPanelExitFadeDurationMs)),
                    ) {
                        Row {
                            HorizontalDraggableDivider(
                                onDrag = { offset ->
                                    rightDragWidth -= with(density) { offset.toDp() }
                                    state = state.copy(rightWidthDp = clampRight(rightDragWidth).value)
                                },
                                onDragStopped = { rightDragWidth = state.rightWidthDp.dp },
                            )
                            Surface(modifier = Modifier.fillMaxHeight().width(state.rightWidthDp.dp)) {
                                when (state.rightPanelTab) {
                                    RightPanelTab.EndpointDetails -> EndpointDetailsWidget(
                                        device = connectedState.activeDevice.device,
                                        activeEndpoint = connectedState.selectedEndpoint,
                                        onCreatePreset = { key ->
                                            viewModel.setSelectedEndpoint(key)
                                            state = state.copy(presetOpen = true, creatingNewPreset = true)
                                        },
                                        onEditPreset = { key ->
                                            viewModel.setSelectedEndpoint(key)
                                            state = state.copy(presetOpen = true, creatingNewPreset = false)
                                        },
                                    )
                                    RightPanelTab.LogDetails -> MonitorLogDetailsWidget(
                                        device = connectedState.activeDevice.device,
                                        logDetail = logDetail,
                                        onClose = {
                                            logDetail = null
                                            state = state.copy(rightPanelTab = null)
                                        },
                                    )
                                    else -> null
                                }
                            }
                        }
                    }

                    // Tab strip always visible on far right
                    VerticalDivider(color = MaterialTheme.colorScheme.outline)
                    VerticalTabList(
                        tabs = listOf(
                            VerticalTab(title = strings.widgets.endpointDetails.title),
                            VerticalTab(title = strings.widgets.logDetails.title),
                        ),
                        clockwise = true,
                        selected = listOfNotNull(RightPanelTab.entries.indexOf(state.rightPanelTab).takeIf { it >= 0 }),
                        onSelect = { index ->
                            val id = RightPanelTab.entries[index]
                            state = state.copy(rightPanelTab = if (state.rightPanelTab == id) null else id)
                            rightDragWidth = state.rightWidthDp.dp
                        },
                    )
                }

                // Scrim behind preset overlay
                ScrimOverlay(visible = presetVisible, onDismiss = { state = state.copy(presetOpen = false) })

                // Create/Edit Preset overlay — Box provides alignment; Column provides ColumnScope
                Box(modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        AnimatedVisibility(
                            visible = presetVisible,
                            enter = slideInHorizontally(animationSpec = tween(animationDuration)) { it },
                            exit = slideOutHorizontally(animationSpec = tween(animationDuration)) { it },
                        ) {
                            connectedState.selectedEndpoint?.let { endpoint ->
                                Row {
                                    HorizontalDraggableDivider(
                                        onDrag = { offset ->
                                            presetDragWidth -= with(density) { offset.toDp() }
                                            state = state.copy(presetWidthDp = max(presetPanelMinWidthDp.dp, presetDragWidth).value)
                                        },
                                        onDragStopped = { presetDragWidth = state.presetWidthDp.dp },
                                    )
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(state.presetWidthDp.dp)
                                            .shadow(8.dp)
                                            .border(1.dp, MaterialTheme.colorScheme.outline),
                                        color = MaterialTheme.colorScheme.surface,
                                    ) {
                                        CreateEditPresetWidget(
                                            device = connectedState.activeDevice.device,
                                            activeEndpoint = endpoint,
                                            creatingNewPreset = state.creatingNewPreset,
                                            onCancel = { state = state.copy(presetOpen = false) },
                                            onSave = { state = state.copy(presetOpen = false) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Scrim behind global controls overlay
                ScrimOverlay(visible = state.globalControlsOpen, onDismiss = { state = state.copy(globalControlsOpen = false) })

                // Global Controls overlay — Box provides alignment; Column provides ColumnScope
                Box(modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)) {
                    Column(modifier = Modifier.fillMaxHeight()) {
                        AnimatedVisibility(
                            visible = state.globalControlsOpen,
                            enter = slideInHorizontally(animationSpec = tween(animationDuration)) { it },
                            exit = slideOutHorizontally(animationSpec = tween(animationDuration)) { it },
                        ) {
                            Row {
                                HorizontalDraggableDivider(
                                    onDrag = { offset ->
                                        globalControlsDragWidth -= with(density) { offset.toDp() }
                                        state = state.copy(globalControlsWidthDp = max(globalControlsPanelMinWidthDp.dp, globalControlsDragWidth).value)
                                    },
                                    onDragStopped = { globalControlsDragWidth = state.globalControlsWidthDp.dp },
                                )
                                Surface(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(state.globalControlsWidthDp.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                ) {
                                    GlobalControlsWidget(
                                        device = connectedState.activeDevice.device,
                                        onClose = { state = state.copy(globalControlsOpen = false) },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Monitor Logs — full width; height driven entirely by widget content
            MonitorLogsWidget(
                modifier = Modifier.fillMaxWidth(),
                device = connectedState.activeDevice.device,
                isExpanded = state.logsExpanded,
                expandedHeightDp = state.logsExpandedHeightDp,
                onExpandToggled = { state = state.copy(logsExpanded = !state.logsExpanded) },
                topHandle = {
                    VerticalDraggableDivider(
                        onDrag = { offset ->
                            logsDragHeight -= with(density) { offset.toDp() }
                            state = state.copy(logsExpandedHeightDp = max(logsMinHeightDp.dp, logsDragHeight).value)
                        },
                        onDragStopped = { logsDragHeight = state.logsExpandedHeightDp.dp },
                    )
                },
                onViewDetail = { logEntry ->
                    logDetail = logEntry
                    state = state.copy(rightPanelTab = RightPanelTab.LogDetails)
                    rightDragWidth = state.rightWidthDp.dp
                },
            )
        }

        AnimatedErrorBanner(
            connectedState.error,
            viewModel::refreshAll,
            viewModel::dismissError,
        )
    }
}

@Composable
private fun LeftTab(
    state: LayoutState,
    connectedState: DeviceRootViewModel.State.Connected
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxHeight().width(state.leftWidthDp.dp)) {
        Column(modifier = Modifier.verticalScroll(scrollState).fillMaxSize()) {
            MetaDataWidget(connectedState.activeDevice.device)
            MiscControlsWidget(connectedState.activeDevice.device)
        }

        PlatformVerticalScrollbar(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}
