package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCardVariant
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.endpointDetailsWidgetSuccessState
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.mockPresets
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity

@Composable
internal fun PresetsContainer(
    state: State.Endpoint,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
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
        if (state.presets.allPresets.isNotEmpty()) {
            PopulatedPresets(
                presets = state.presets,
                onPresetFilterChanged = onPresetFilterChanged,
                onDefaultPresetSelected = onDefaultPresetSelected,
                onEditPreset = onEditPreset,
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
                    label = strings.moreInfoButton,
                    onClick = onPresetMoreInfoClicked,
                    variant = OutlineButtonVariant.Secondary,
                )
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
    presets: State.Endpoint.Presets,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onEditPreset: () -> Unit = {},
    layoutMode: RowDensity = RowDensity.Compact,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (presets.allPresets.size > 1) {
            BasicTextField(
                value = presets.filter,
                onValueChange = onPresetFilterChanged,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceMuted,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Box {
                            if (presets.filter.isEmpty()) {
                                Text(
                                    text = strings.filterPlaceholder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceMuted,
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
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
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        presets.visiblePresets.forEachIndexed { index, preset ->
            if (index == 0) {
                Spacer(Modifier.height(4.dp))
            } else {
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
                layoutMode = layoutMode
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
