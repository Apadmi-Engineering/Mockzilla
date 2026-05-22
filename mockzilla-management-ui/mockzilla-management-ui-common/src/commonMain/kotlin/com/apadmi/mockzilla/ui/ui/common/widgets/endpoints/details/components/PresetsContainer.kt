package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.PresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCardVariant
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State.Endpoint.LayoutMode
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.endpointDetailsWidgetSuccessState
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.mockPresets
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun PresetsContainer(
    state: State.Endpoint,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
    showBorder: Boolean = true,
    showTitle: Boolean = true,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) {
    val isCompact = state.layoutMode == LayoutMode.Compact
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (showBorder) {
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (state.config.shouldFail == true) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
            } else {
                Modifier
            })
    ) {
        Column(
            modifier = Modifier
                .padding(if (showBorder) 16.dp else 0.dp)
                .then(
                    if (isCompact) {
                        Modifier.heightIn(max = 400.dp).verticalScroll(scrollState)
                    } else {
                        Modifier
                    }
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.presets.allPresets.isNotEmpty()) {
                PopulatedPresets(
                    presets = state.presets,
                    onPresetFilterChanged = onPresetFilterChanged,
                    onDefaultPresetSelected = onDefaultPresetSelected,
                    showTitle = showTitle,
                    layoutMode = state.layoutMode
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = strings.noAvailablePresetsTitle,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = strings.noAvailablePresetsBody,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    CustomOutlineButton(
                        label = "More Information",
                        onClick = onPresetMoreInfoClicked,
                        variant = OutlineButtonVariant.Secondary,
                    )
                }
            }
        }

        if (state.config.shouldFail == true) {
            ForcedFailureOverlayBanner(borderShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        }
    }
}

@Composable
private fun PopulatedPresets(
    presets: State.Endpoint.Presets,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    showTitle: Boolean = true,
    layoutMode: LayoutMode = LayoutMode.Compact,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) {
    Column {
        if (showTitle) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = strings.title
            )
        }
        if (presets.allPresets.size > 1) {
            if (showTitle) {
                Spacer(Modifier.size(8.dp))
            }
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = presets.filter,
                onValueChange = onPresetFilterChanged,
                prefix = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                placeholder = { Text(strings.filterPlaceholder) }
            )
        }
        Spacer(Modifier.size(8.dp))
    }

    if (presets.visiblePresets.isEmpty()) {
        Text(strings.filterPlaceholderEmpty)
    }

    presets.visiblePresets.forEach { preset ->
        PresetCard(
            variant = if (preset.name == presets.appliedPreset?.name) {
                PresetCardVariant.Selected
            } else {
                PresetCardVariant.Selectable
            },
            preset = preset,
            onClicked = onDefaultPresetSelected,
            layoutMode = layoutMode
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview
@Composable
private fun PresetsContainerPreview() = PreviewSurface {
    PresetsContainerPreviewContainer()
}

@Preview
@Composable
private fun PresetsContainerDarkPreview() = PreviewSurface(darkTheme = true) {
    PresetsContainerPreviewContainer()
}

@Preview
@Composable
private fun PresetsContainerEmptySearchPreview() = PreviewSurface {
    PresetsContainerPreviewContainer(
        presets = State.Endpoint.Presets(
            appliedPreset = null,
            visiblePresets = emptyList(),
            allPresets = mockPresets,
            filter = "search term"
        )
    )
}

@Preview
@Composable
private fun PresetsContainerEmptyPreview() = PreviewSurface {
    PresetsContainerPreviewContainer(
        presets = State.Endpoint.Presets(
            appliedPreset = null,
            visiblePresets = emptyList(),
            allPresets = emptyList(),
            filter = ""
        )
    )
}

@Preview
@Composable
private fun PresetsContainerForceFailurePreview() = PreviewSurface {
    PresetsContainerPreviewContainer(fail = true)
}

@Preview
@Composable
private fun PresetsContainerForceFailureDarkPreview() = PreviewSurface(darkTheme = true) {
    PresetsContainerPreviewContainer(fail = true)
}

@Composable
private fun PresetsContainerPreviewContainer(
    presets: State.Endpoint.Presets = endpointDetailsWidgetSuccessState().presets,
    fail: Boolean = false
) = PreviewSurface {
    PresetsContainer(
        state = endpointDetailsWidgetSuccessState(fail = fail).copy(presets = presets),
        onPresetFilterChanged = {},
        onDefaultPresetSelected = {},
        onPresetMoreInfoClicked = {}
    )
}
