@file:Suppress("MAGIC_NUMBER", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.Clock
import com.apadmi.mockzilla.ui.ui.common.assets.EditUnderscore
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.EdSection
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBanner
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.components.SurfaceHeader
import com.apadmi.mockzilla.ui.ui.common.components.Tag
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.statusColors
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.mockzillaMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.*
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State.Endpoint.LayoutMode
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components.PresetsContainer
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties

import org.koin.core.parameter.parametersOf

private val tealColor = Color(0xFF_0D9_488)

@Composable
private fun ColumnScope.PopulatedState(
    state: State.Endpoint,
    strings: Strings,
    onResetAll: () -> Unit,
    onFailChange: (Boolean?) -> Unit,
    onDelayChange: (Int?) -> Unit,
    onFilterPresetChanged: (String) -> Unit,
    onLayoutModeChanged: (LayoutMode) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onCreatePreset: () -> Unit,
    onEditPreset: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalForceDarkMode.current
    val accentColor = if (isDark) colorScheme.primary else tealColor

    Box {
        SurfaceHeader(
            title = state.config.name,
            subtitle = strings.widgets.endpointDetails.subtitle,
            actions = {
                BaseButton(
                    label = strings.widgets.endpointDetails.reset,
                    leadingIcon = Icons.Default.Refresh,
                    variant = ButtonVariant.Ghost,
                    size = ButtonSize.Sm,
                    contentColor = if (isDark) colorScheme.onSurface else null,
                    onClick = onResetAll,
                )
            },
            content = {
                val overrides = state.config.getOverriddenProperties()
                val isForced = state.config.shouldFail == true
                if (overrides.isEmpty() && !isForced) {
                    Text(
                        text = strings.widgets.endpoints.noOverrides,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = mockzillaMonoFontFamily(),
                            color = colorScheme.onSurfaceMuted,
                            fontWeight = FontWeight.Normal,
                        ),
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (isForced) {
                            Tag(
                                label = strings.widgets.endpoints.forced,
                                textColor = colorScheme.error,
                                borderColor = colorScheme.error.copy(alpha = 0.5f),
                                backgroundColor = colorScheme.error.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        overrides.forEach { property ->
                            Tag(
                                label = property.displayName.uppercase(),
                                textColor = accentColor,
                                borderColor = accentColor.copy(alpha = 0.5f),
                                backgroundColor = accentColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        )

        Box(Modifier.height(12.dp).fillMaxWidth().clipToBounds()) {
            TogglableProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isLoading,
                trackColor = Color.Transparent
            )
        }
    }

    state.presets.appliedPreset?.let { preset ->
        ActivePresetBanner(preset = preset, onClear = onResetAll, onEdit = onEditPreset)
    }

    EdSection(
        label = strings.widgets.endpointDetails.behavior,
        icon = Icons.LightningBolt,
    ) {
        ForceFailureBanner(
            state = if (state.config.shouldFail == true) {
                ForceFailureBannerState.FullFailure
            } else {
                ForceFailureBannerState.Normal
            },
            onRestoreApiClicked = { onFailChange(false) },
            onForceFailureClicked = { onFailChange(true) },
        )
    }

    EdSection(
        label = strings.widgets.endpointDetails.latency,
        icon = Icons.Clock,
    ) {
        ResponseLatencyCard(
            initialValue = state.config.delayMs,
            onChange = onDelayChange,
            onReset = { onDelayChange(null) },
            showHeader = false,
            showBackground = false,
            showBorder = false,
        )
    }

    EdSection(
        label = "${strings.widgets.endpointDetails.presets.title} (${state.presets.allPresets.size})",
        icon = Icons.Default.DragIndicator,
        contentPadding = PaddingValues(0.dp),
        headerActions = {
            Row(
                modifier = Modifier
                    .border(1.dp, colorScheme.outline, RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isDark) MaterialTheme.colorScheme.surfaceContainer else Color(0xFF_D8D_CE1))
                    .padding(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(LayoutMode.Comfy to "comfy", LayoutMode.Compact to "compact").forEach { (mode, label) ->
                    val isSelected = state.layoutMode == mode
                    val chipShape = RoundedCornerShape(4.dp)
                    Box(
                        modifier = Modifier
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .border(1.dp, colorScheme.outline, chipShape)
                                        .clip(chipShape)
                                } else {
                                    Modifier.clip(chipShape)
                                }
                            )
                            .background(
                                if (isSelected) {
                                    if (isDark) MaterialTheme.colorScheme.surfaceContainerHigh else Color.White
                                } else {
                                    Color.Transparent
                                }
                            )
                            .clickable { onLayoutModeChanged(mode) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onCreatePreset)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = "Custom",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PresetsContainer(
                state = state,
                onPresetFilterChanged = onFilterPresetChanged,
                onDefaultPresetSelected = onDefaultPresetSelected,
                onPresetMoreInfoClicked = onPresetMoreInfoClicked,
                onEditPreset = onEditPreset,
                showBorder = false,
                showTitle = false
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

private fun SerializableEndpointConfig.getOverriddenProperties() = listOfNotNull(
    EndpointProperties.Delay.takeIf { delayMs != null },
    EndpointProperties.Body.takeIf { defaultBody != null || appliedPresetOverride?.response?.body != null },
    EndpointProperties.Status.takeIf { defaultStatus != null || appliedPresetOverride?.response?.statusCode != null },
    EndpointProperties.Headers.takeIf { defaultHeaders != null || appliedPresetOverride?.response?.headers != null }
)

@Composable
fun EndpointDetailsWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key?,
    onCreatePreset: (EndpointConfiguration.Key) -> Unit,
    onEditPreset: (EndpointConfiguration.Key) -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    val viewModel = getViewModel<EndpointDetailsViewModel>(
        key = "${activeEndpoint?.raw}-$device"
    ) { parametersOf(activeEndpoint, device) }
    val state by viewModel.state.collectAsState()

    EndpointDetailsWidgetContent(
        state = state,
        onDelayChange = viewModel::updateLatency,
        onFailChange = viewModel::onFailChange,
        onDefaultPresetSelected = viewModel::onPresetSelected,
        onResetAll = viewModel::onResetAll,
        onFilterPresetChanged = viewModel::onFilterPresetChanged,
        onLayoutModeChanged = viewModel::onLayoutModeChanged,
        onCreatePreset = { activeEndpoint?.let { onCreatePreset(activeEndpoint) } },
        onEditPreset = { activeEndpoint?.let { onEditPreset(activeEndpoint) } },
        onPresetMoreInfoClicked = {
            // TODO, Add preset docs and update link
            uriHandler.openUri("https://mockzilla.apadmi.dev/")
        }
    )
}

@Composable
internal fun EndpointDetailsWidgetContent(
    state: State,
    onDelayChange: (Int?) -> Unit,
    onFailChange: (Boolean?) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onResetAll: () -> Unit,
    onFilterPresetChanged: (String) -> Unit,
    onLayoutModeChanged: (LayoutMode) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onCreatePreset: () -> Unit,
    onEditPreset: () -> Unit = {},
    strings: Strings = LocalStrings.current,
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(color = colorScheme.surface)
    ) {
        when (state) {
            is State.Empty -> EmptyState(
                title = strings.widgets.endpointDetails.emptyTitle,
                description = strings.widgets.endpointDetails.emptyDescription,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            )

            is State.Endpoint -> Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                PopulatedState(
                    state,
                    strings,
                    onResetAll,
                    onFailChange,
                    onDelayChange,
                    onFilterPresetChanged,
                    onLayoutModeChanged,
                    onDefaultPresetSelected,
                    onPresetMoreInfoClicked,
                    onCreatePreset,
                    onEditPreset,
                )
            }
        }
    }
}

@Composable
private fun ActivePresetBanner(
    preset: DashboardOverridePreset,
    onClear: () -> Unit,
    onEdit: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalForceDarkMode.current
    val statusColors = preset.statusColors()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(statusColors.primary.copy(alpha = 0.08f))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = statusColors.primary,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
                if (isDark) {
                    drawLine(
                        color = colorScheme.outline,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
            .padding(start = 16.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = statusColors.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = preset.name,
            style = MaterialTheme.typography.titleSmall,
            color = statusColors.primary,
            fontWeight = FontWeight.Bold,
        )
        preset.response.statusCode?.let {
            Tag(
                label = it.value.toString(),
                textColor = statusColors.primary,
                borderColor = statusColors.primary,
                backgroundColor = Color.Transparent,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        Text(
            text = "Preset",
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable { onEdit() }
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.EditUnderscore,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Edit",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(heightDp = 1000)
@Composable
private fun EndpointDetailsWidgetEmptyPreview() = PreviewSurface {
    EndpointDetailsWidgetPreviewContent(state = State.Empty)
}

@Preview(heightDp = 1110)
@Composable
private fun EndpointDetailsWidgetPreview() = PreviewSurface {
    EndpointDetailsWidgetPreviewContent(state = endpointDetailsWidgetSuccessState())
}

@Preview(heightDp = 1110)
@Composable
private fun EndpointDetailsWidgetDarkPreview() = PreviewSurface(darkTheme = true) {
    EndpointDetailsWidgetPreviewContent(state = endpointDetailsWidgetSuccessState())
}

@Preview(heightDp = 1110)
@Composable
private fun EndpointDetailsWidgetForceFailurePreview() = PreviewSurface {
    EndpointDetailsWidgetPreviewContent(
        state = endpointDetailsWidgetSuccessState(fail = true)
    )
}

@Preview(heightDp = 1110)
@Composable
private fun EndpointDetailsWidgetForceFailureDarkPreview() = PreviewSurface(darkTheme = true) {
    EndpointDetailsWidgetPreviewContent(
        state = endpointDetailsWidgetSuccessState(fail = true)
    )
}
