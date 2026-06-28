@file:Suppress("MAGIC_NUMBER", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.isOverflowingLatencySlider
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.Clock
import com.apadmi.mockzilla.ui.ui.common.assets.EditUnderscore
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.EndpointDetailsSection
import com.apadmi.mockzilla.ui.ui.common.components.ErrorRetry
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBanner
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.components.PlatformVerticalScrollbar
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.components.SurfaceHeader
import com.apadmi.mockzilla.ui.ui.common.components.Tag
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.RowDensityControls
import com.apadmi.mockzilla.ui.ui.common.components.statusColors
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.mockzillaMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.*
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components.PresetsContainer
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity
import com.apadmi.mockzilla.ui.utils.iconButtonSize

import org.koin.core.parameter.parametersOf

@Composable
private fun ColumnScope.PopulatedState(
    state: State.Endpoint,
    onResetAll: () -> Unit,
    onFailChange: (Boolean?) -> Unit,
    onDelayChange: (Int?) -> Unit,
    onFilterPresetChanged: (String) -> Unit,
    onRowDensityChanged: (RowDensity) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onCreatePreset: () -> Unit,
    onEditPreset: () -> Unit = {},
    strings: Strings.Widgets = LocalStrings.current.widgets,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalForceDarkMode.current
    val accentColor = colorScheme.primary

    Box {
        SurfaceHeader(
            title = state.config.name,
            subtitle = strings.endpointDetails.subtitle,
            actions = {
                BaseButton(
                    label = strings.endpointDetails.reset,
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
                        text = strings.endpoints.noOverrides,
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
                                label = strings.endpoints.forced,
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
        ActivePresetBanner(
            isForceFailureEnabled = state.config.shouldFail == true,
            preset = preset,
            onClear = onResetAll,
            onEdit = onEditPreset
        )
    }

    EndpointDetailsSection(
        label = strings.endpointDetails.behavior,
        icon = Icons.LightningBolt,
        headerActions = {
            // Invisible control just to ensure the rows are a consistent height
            RowDensityControls(modifier = Modifier.alpha(0f).clearAndSetSemantics { /* No-Op*/ })
        }
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

    EndpointDetailsSection(
        label = strings.endpointDetails.latency,
        icon = Icons.Clock,
        headerActions = {
            // Invisible control just to ensure the rows are a consistent height
            RowDensityControls(modifier = Modifier.alpha(0f).clearAndSetSemantics { /* No-Op*/ })
        }
    ) {
        ResponseLatencyCard(
            initialValue = state.config.delayMs,
            isOverflowing = state.config.delayMs.isOverflowingLatencySlider(),
            onChange = onDelayChange,
            onReset = { onDelayChange(null) },
            showHeader = false,
            showBackground = false,
            showBorder = false,
        )
    }

    EndpointDetailsSection(
        label = "${strings.endpointDetails.presets.title} (${state.presets.allPresets.size})",
        icon = Icons.Default.DragIndicator,
        contentPadding = PaddingValues(0.dp),
        headerActions = {
            if (state.presets.allPresets.isNotEmpty()) {
                RowDensityControls(
                    selected = state.layoutMode,
                    onChanged = onRowDensityChanged
                )
            }
            Spacer(Modifier.width(8.dp))
            BaseButton(
                variant = ButtonVariant.Ghost,
                size = ButtonSize.Sm,
                leadingIcon = Icons.Default.Add,
                label = strings.endpointDetails.presets.typeDescriptions.other,
                enabled = state.config.shouldFail != true,
                onClick = onCreatePreset,
            )
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (state.config.shouldFail == true) {
                ForceFailurePresetBanner()
            }
            PresetsContainer(
                state = state,
                onPresetFilterChanged = onFilterPresetChanged,
                onDefaultPresetSelected = onDefaultPresetSelected,
                onPresetMoreInfoClicked = onPresetMoreInfoClicked,
                onEditPreset = onEditPreset
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
        device = device,
        keyPrefix = activeEndpoint?.raw
    ) { parametersOf(activeEndpoint, device) }
    val state by viewModel.state.collectAsState()

    EndpointDetailsWidgetContent(
        state = state,
        onDelayChange = viewModel::updateLatency,
        onFailChange = viewModel::onFailChange,
        onDefaultPresetSelected = viewModel::onPresetSelected,
        onResetAll = viewModel::onResetAll,
        onFilterPresetChanged = viewModel::onFilterPresetChanged,
        onRowDensityChanged = viewModel::onRowDensityChanged,
        onCreatePreset = { activeEndpoint?.let { onCreatePreset(activeEndpoint) } },
        onEditPreset = { activeEndpoint?.let { onEditPreset(activeEndpoint) } },
        onRetry = viewModel::retry,
        onPresetMoreInfoClicked = {
            uriHandler.openUri("https://mockzilla.apadmi.dev/presets/")
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
    onRowDensityChanged: (RowDensity) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onCreatePreset: () -> Unit,
    onEditPreset: () -> Unit = {},
    onRetry: () -> Unit = {},
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
            is State.FailedToLoad -> ErrorRetry(
                modifier = Modifier.align(Alignment.Center),
                onRetry = onRetry
            )
            is State.Empty -> EmptyState(
                title = strings.widgets.endpointDetails.emptyTitle,
                description = strings.widgets.endpointDetails.emptyDescription,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceFaint
                    )
                }
            )

            is State.Endpoint -> {
                val scrollState = rememberScrollState()
                Box(modifier = Modifier.fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(scrollState)
                    ) {
                        PopulatedState(
                            state = state,
                            onResetAll = onResetAll,
                            onFailChange = onFailChange,
                            onDelayChange = onDelayChange,
                            onFilterPresetChanged = onFilterPresetChanged,
                            onRowDensityChanged = onRowDensityChanged,
                            onDefaultPresetSelected = onDefaultPresetSelected,
                            onPresetMoreInfoClicked = onPresetMoreInfoClicked,
                            onCreatePreset = onCreatePreset,
                            onEditPreset = onEditPreset,
                        )
                    }
                    PlatformVerticalScrollbar(
                        scrollState = scrollState,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun ForceFailurePresetBanner(
    strings: Strings = LocalStrings.current
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.errorContainer)
        .padding(horizontal = 12.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        modifier = Modifier.size(12.dp),
        imageVector = Icons.Outlined.Lock,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error
    )
    Spacer(Modifier.width(4.dp))
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            ) {
                append(strings.widgets.endpointDetails.presets.forceFailureBannerTitle)
            }
            append(" ")
            withStyle(
                style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                append(strings.widgets.endpointDetails.presets.forceFailureBannerBody)
            }
        },

        textAlign = TextAlign.Start
    )
}

@Composable
private fun ActivePresetBanner(
    isForceFailureEnabled: Boolean,
    preset: DashboardOverridePreset,
    onClear: () -> Unit,
    onEdit: () -> Unit = {},
    strings: Strings = LocalStrings.current
) {
    val colorScheme = MaterialTheme.colorScheme
    val statusColors = preset.statusColors()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(statusColors.primary.copy(alpha = 0.08f))
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
            modifier = Modifier.weight(1f),
            text = preset.name,
            style = MaterialTheme.typography.titleSmall,
            color = statusColors.primary,
            overflow = TextOverflow.Ellipsis,
            textDecoration = if (isForceFailureEnabled) TextDecoration.LineThrough else null,
            fontWeight = FontWeight.Bold,
        )
        preset.response.statusCode?.takeUnless { isForceFailureEnabled }
            ?.let {
                Tag(
                    label = it.value.toString(),
                    textColor = statusColors.primary,
                    borderColor = statusColors.primary,
                    backgroundColor = Color.Transparent,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                )
            }

        if (isForceFailureEnabled) {
            Text(
                text = strings.widgets.endpointDetails.presets.forceFailureAppliedPresetMessage,
                style = MaterialTheme.typography.titleSmall,
                color = colorScheme.error,
                fontWeight = FontWeight.Bold,
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable { onEdit() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
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
        IconButton(onClick = onClear, modifier = Modifier.iconButtonSize(24.dp)) {
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
