package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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

import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsViewModel.State

import org.koin.core.parameter.parametersOf

@Composable
fun GlobalControlsWidget(device: Device, onClose: () -> Unit = {}) {
    val viewModel = getViewModel<GlobalControlsViewModel>(key = device.toString()) {
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

@Suppress("MAGIC_NUMBER")
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
    val isDark = LocalForceDarkMode.current
    val backgroundColor = if (isDark) colorScheme.surface else Color.White
    val tealAccent = Color(0xFF_0D9_488)
    val accentColor = if (isDark) colorScheme.primary else tealAccent

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = backgroundColor)
            .navigationBarsPadding(),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = colorScheme.outline.copy(alpha = 0.15f),
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = strings.common.backDescription,
                        tint = colorScheme.onSurface
                    )
                }
            }

            Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                Text(
                    text = strings.widgets.globalControls.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = strings.widgets.globalControls.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = strings.widgets.globalControls.activeOverrides(state.activeOverridesCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.weight(0.05f))

            BaseButton(
                modifier = Modifier.height(32.dp),
                label = strings.widgets.globalControls.resetAllLabel,
                variant = ButtonVariant.Outline,
                size = ButtonSize.Sm,
                leadingIcon = Icons.Default.Refresh,
                onClick = onResetClicked
            )

            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = colorScheme.outline.copy(alpha = 0.15f),
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = strings.common.closeDescription,
                        tint = colorScheme.onSurface
                    )
                }
            }
        }

        HorizontalDivider(color = colorScheme.outline.copy(alpha = 0.3f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(backgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Force Failure Card
            ForceFailureCard(
                state = state.apiFailureState,
                onRestoreApiClicked = onRestoreApiClicked,
                onForceFailureClicked = onForceFailureClicked,
                strings = strings
            )

            // Response Latency Card
            ResponseLatencyCard(
                initialValue = state.initialLatencyMs,
                onChange = onLatencyChanged,
                onReset = onResetLatency,
                strings = strings,
                showClearButton = false
            )

            // Per-Endpoint Status Section
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = strings.widgets.globalControls.perEndpointStatus,
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
                    color = colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                state.endpoints.filter { it.overriddenProperties.isNotEmpty() || it.fail }.forEach { endpoint ->
                    EndpointStatusRow(endpoint, strings, accentColor)
                }
            }
        }
    }
}

@Composable
private fun ForceFailureCard(
    state: ForceFailureBannerState,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    strings: Strings
) {
    val colorScheme = MaterialTheme.colorScheme
    val titleAndSubtitle = when (state) {
        ForceFailureBannerState.FullFailure -> strings.widgets.globalControls.forcedFailureBannerConfig
        ForceFailureBannerState.PartialFailure -> strings.widgets.globalControls.partialFailureBannerConfig
        ForceFailureBannerState.Normal -> strings.widgets.globalControls.normalBehaviourBannerConfig
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp))
            .border(1.dp, colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = titleAndSubtitle.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            Text(
                text = titleAndSubtitle.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }

        if (state == ForceFailureBannerState.Normal) {
            BaseButton(
                modifier = Modifier.fillMaxWidth(),
                label = strings.widgets.globalControls.failButtonLabel,
                variant = ButtonVariant.Outline,
                size = ButtonSize.Md,
                leadingIcon = Icons.LightningBolt,
                contentColor = colorScheme.error,
                onClick = onForceFailureClicked
            )
        } else {
            BaseButton(
                modifier = Modifier.fillMaxWidth(),
                label = strings.widgets.globalControls.restoreButtonLabel,
                variant = ButtonVariant.Solid,
                size = ButtonSize.Md,
                onClick = onRestoreApiClicked
            )
        }
    }
}

@Composable
private fun EndpointStatusRow(
    endpoint: EndpointsViewModel.State.EndpointConfig,
    strings: Strings,
    accentColor: Color
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorScheme.outline.copy(alpha = 0.05f), shape = RoundedCornerShape(8.dp))
            .border(1.dp, colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = endpoint.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (endpoint.fail) {
                StatusChip(label = strings.widgets.globalControls.forcedStatus, color = colorScheme.error)
            }
            endpoint.overriddenProperties.forEach { property ->
                StatusChip(
                    label = when (property) {
                        EndpointProperties.Delay -> strings.widgets.globalControls.latencyStatus
                        EndpointProperties.Body -> strings.widgets.globalControls.bodyStatus
                        EndpointProperties.Headers -> strings.widgets.globalControls.headersStatus
                        EndpointProperties.Status -> strings.widgets.globalControls.statusStatus
                    },
                    color = accentColor
                )
            }
        }
    }
}

@Composable
private fun StatusChip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
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
