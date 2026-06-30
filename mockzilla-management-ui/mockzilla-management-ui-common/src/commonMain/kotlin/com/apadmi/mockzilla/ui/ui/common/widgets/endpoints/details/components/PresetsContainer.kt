package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ErrorRetry
import com.apadmi.mockzilla.ui.ui.common.components.FilterTextField
import com.apadmi.mockzilla.ui.ui.common.components.PresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCardVariant
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.RowDensityControls
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.endpointDetailsWidgetSuccessState
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.mockPresets
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity
import com.apadmi.mockzilla.ui.utils.Platform

@Composable
internal fun PresetsContainer(
    state: State.Endpoint,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onRetry: () -> Unit,
    onRowDensityChanged: (RowDensity) -> Unit,
    onEditPreset: () -> Unit = {},
    modifier: Modifier = Modifier,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) = Box(
    modifier = modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
) {
    Column(
        verticalArrangement = Arrangement.Top
    ) {
        when (val presets = state.presets) {
            is State.Endpoint.Presets.Loading -> Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            is State.Endpoint.Presets.Error -> Box(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                ErrorRetry(onRetry = onRetry)
            }
            is State.Endpoint.Presets.Populated -> if (presets.allPresets.isNotEmpty()) {
                PopulatedPresets(
                    presets = presets,
                    onPresetFilterChanged = onPresetFilterChanged,
                    onDefaultPresetSelected = onDefaultPresetSelected,
                    onEditPreset = onEditPreset,
                    rowDensity = state.layoutMode,
                    onRowDensityChanged = onRowDensityChanged
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
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
                    CustomButton(
                        label = strings.moreInfoButton,
                        onClick = onPresetMoreInfoClicked,
                        variant = ButtonVariant.Outline,
                    )
                }
            }
        }
    }

    if (state.config.shouldFail == true) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            event.changes.forEach { it.consume() }
                        }
                    }
                }
        )
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun PopulatedPresets(
    presets: State.Endpoint.Presets.Populated,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onEditPreset: () -> Unit = {},
    rowDensity: RowDensity = RowDensity.Compact,
    onRowDensityChanged: (RowDensity) -> Unit,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (presets.allPresets.size > 1) {
            FilterTextField(
                modifier = Modifier.weight(1f),
                value = presets.filter,
                onFilterUpdate = onPresetFilterChanged,
                placeholder = strings.filterPlaceholder
            )

            if (Platform.current != Platform.Desktop) {
                Spacer(Modifier.width(4.dp))

                RowDensityControls(
                    selected = rowDensity,
                    onChanged = onRowDensityChanged
                )
            }
        }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (presets.visiblePresets.isEmpty()) {
            Text(
                text = strings.filterPlaceholderEmpty,
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onSurfaceMuted,
                style = MaterialTheme.typography.labelMedium
            )
        }

        presets.visiblePresets.forEachIndexed { index, preset ->
            if (index != 0) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            }
            PresetCard(
                variant = if (preset.name == presets.appliedPreset?.name) {
                    PresetCardVariant.Selected
                } else {
                    PresetCardVariant.Selectable
                },
                preset = preset,
                onClicked = onDefaultPresetSelected,
                onEdit = onEditPreset,
                rowDensity = rowDensity
            )
            if (index == presets.visiblePresets.lastIndex) {
                Spacer(Modifier.height(4.dp))
            }
        }
    }
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
        presets = State.Endpoint.Presets.Populated(
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
        presets = State.Endpoint.Presets.Populated(
            appliedPreset = null,
            visiblePresets = emptyList(),
            allPresets = emptyList(),
            filter = ""
        )
    )
}

@Preview
@Composable
private fun PresetsContainerLoadingPreview() = PreviewSurface {
    PresetsContainerPreviewContainer(
        presets = State.Endpoint.Presets.Loading
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
    presets: State.Endpoint.Presets = (endpointDetailsWidgetSuccessState().presets as State.Endpoint.Presets.Populated),
    fail: Boolean = false
) = PreviewSurface {
    PresetsContainer(
        state = endpointDetailsWidgetSuccessState(fail = fail).copy(presets = presets),
        onPresetFilterChanged = {},
        onDefaultPresetSelected = {},
        onPresetMoreInfoClicked = {},
        onRetry = {},
        onRowDensityChanged = {}
    )
}
