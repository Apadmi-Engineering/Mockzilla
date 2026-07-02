package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.isOverflowingLatencySlider
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBanner
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomButton
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsViewModel.State
import com.apadmi.mockzilla.ui.utils.Platform
import com.apadmi.mockzilla.ui.utils.iconButtonSize

import org.koin.core.parameter.parametersOf

@InternalMockzillaApi
@Composable
public fun GlobalControlsWidget(device: Device, onClose: () -> Unit = {}) {
    val viewModel = getViewModel<GlobalControlsViewModel>(device = device) {
        parametersOf(device)
    }
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsState()

    GlobalControlsWidgetContent(
        state = state,
        onClose = onClose,
        onResetClicked = {
            focusManager.clearFocus()
            viewModel.resetAll()
        },
        onRestoreApiClicked = viewModel::restoreApi,
        onForceFailureClicked = viewModel::forceFailure,
        onLatencyChanged = viewModel::updateLatency,
        onResetLatency = {
            focusManager.clearFocus()
            viewModel.resetLatency()
        },
    )
}

@Composable
internal fun GlobalControlsWidgetContent(
    state: State,
    onClose: () -> Unit,
    onResetClicked: () -> Unit,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    onLatencyChanged: (Int) -> Unit,
    onResetLatency: () -> Unit,
) = when (state) {
    is State.Idle -> GlobalControlsWidgetIdleContent(
        state,
        onClose = onClose,
        onResetClicked = onResetClicked,
        onRestoreApiClicked = onRestoreApiClicked,
        onForceFailureClicked = onForceFailureClicked,
        onLatencyChanged = onLatencyChanged,
        onResetLatency = onResetLatency,
    )

    State.Loading -> Box(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
internal fun GlobalControlsWidgetIdleContent(
    state: State.Idle,
    onClose: () -> Unit,
    strings: Strings = LocalStrings.current,
    onResetClicked: () -> Unit,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    onLatencyChanged: (Int) -> Unit,
    onResetLatency: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = colorScheme.surface)
            .navigationBarsPadding(),
    ) {
        // Header
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = strings.widgets.globalControls.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                FlowRow(itemVerticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = strings.widgets.globalControls.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = strings.widgets.globalControls.activeOverrides(state.activeOverridesCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            CustomButton(
                label = strings.widgets.globalControls.resetAllLabel,
                variant = ButtonVariant.Ghost,
                size = ButtonSize.Sm,
                leadingIcon = Icons.Default.Refresh,
                onClick = onResetClicked
            )

            if (Platform.current == Platform.Desktop) {
                IconButton(
                    modifier = Modifier.iconButtonSize(),
                    onClick = onClose
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = strings.common.closeDescription,
                        tint = colorScheme.onSurfaceFaint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = colorScheme.outline)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ForceFailureBanner(
                state = state.apiFailureState,
                onRestoreApiClicked = onRestoreApiClicked,
                onForceFailureClicked = onForceFailureClicked,
                strings = strings
            )

            Box(
                Modifier.border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(8.dp)
                )
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                ResponseLatencyCard(
                    initialValue = state.initialLatencyMs,
                    isOverflowing = state.initialLatencyMs.isOverflowingLatencySlider(),
                    onChange = onLatencyChanged,
                    onReset = onResetLatency,
                    strings = strings
                )
            }

            // Per-Endpoint Status Section
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredEndpoints = state.endpoints.filter { it.overriddenProperties.isNotEmpty() || it.fail }

                if (filteredEndpoints.isNotEmpty()) {
                    Text(
                        text = strings.widgets.globalControls.perEndpointStatus,
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    filteredEndpoints.forEach { endpoint ->
                        EndpointStatusRow(endpoint, strings)
                    }
                }
            }
        }
    }
}

@Composable
private fun EndpointStatusRow(
    endpoint: EndpointsViewModel.State.EndpointConfig,
    strings: Strings,
    colorScheme: ColorScheme = MaterialTheme.colorScheme
) = FlowRow(
    modifier = Modifier
        .fillMaxWidth()
        .background(color = colorScheme.surfaceContainer, shape = RoundedCornerShape(8.dp))
        .border(1.dp, colorScheme.outline, RoundedCornerShape(8.dp))
        .padding(horizontal = 12.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    Text(
        text = endpoint.name,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = colorScheme.onSurface
    )

    Spacer(Modifier.weight(1f))

    if (endpoint.fail) {
        StatusChip(
            label = strings.widgets.globalControls.forcedStatus,
            color = colorScheme.error
        )
    }
    endpoint.overriddenProperties.forEach { property ->
        StatusChip(
            label = when (property) {
                EndpointProperties.Delay -> strings.widgets.globalControls.latencyStatus
                EndpointProperties.Body -> strings.widgets.globalControls.bodyStatus
                EndpointProperties.Headers -> strings.widgets.globalControls.headersStatus
                EndpointProperties.Status -> strings.widgets.globalControls.statusStatus
            },
            color = when (property) {
                EndpointProperties.Delay -> colorScheme.warning.primary
                else -> colorScheme.primary
            }
        )
    }
}

@Composable
private fun StatusChip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .border(0.5.dp, color, RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.ExtraBold),
            color = color
        )
    }
}

@Preview
@Composable
private fun GlobalControlsWidgetPreview() = PreviewSurface {
    GlobalControlsWidgetContent(
        State.Idle(
            10,
            isLoading = false,
            apiFailureState = ForceFailureBannerState.FullFailure,
            endpoints = emptyList(),
            activeOverridesCount = 4
        ),
        onClose = {},
        onResetClicked = {},
        onRestoreApiClicked = {},
        onForceFailureClicked = {},
        onLatencyChanged = {},
        onResetLatency = {},
    )
}
