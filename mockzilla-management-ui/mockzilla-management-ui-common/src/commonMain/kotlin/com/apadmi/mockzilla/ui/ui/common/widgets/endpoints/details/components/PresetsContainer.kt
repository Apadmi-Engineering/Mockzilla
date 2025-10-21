package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.NoPresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCardVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State

@Composable
internal fun PresetsContainer(
    state: State.Endpoint,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) = Box(
    modifier = modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)
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
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (state.presets.allPresets.isNotEmpty()) {
            PopulatedPresets(
                presets = state.presets,
                onPresetFilterChanged = onPresetFilterChanged,
                onDefaultPresetSelected = onDefaultPresetSelected
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
                CustomOutlineButton(label = "More Information", onClick = onPresetMoreInfoClicked)
            }
        }
    }

    if (state.config.shouldFail == true) {
        ForcedFailureOverlayBanner(borderShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
    }
}

@Composable
internal fun ActivePresetCard(
    modifier: Modifier = Modifier,
    state: State.Endpoint,
    onEditPreset: (DashboardOverridePreset) -> Unit,
    strings: Strings.Widgets.EndpointDetails = LocalStrings.current.widgets.endpointDetails
) = Box(
    modifier = modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)
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
) {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(strings.presets.activePresetTitle, style = MaterialTheme.typography.titleMedium)
            CustomOutlineButton(
                label = strings.presets.createCustomButton,
                leadingIcon = rememberVectorPainter(Icons.Outlined.AddCircle),
                onClick = { }  // TODO - Add custom preset support
            )
        }

        state.presets.appliedPreset?.let {
            PresetCard(
                variant = PresetCardVariant.Selected,
                preset = state.presets.appliedPreset,
                onClicked = onEditPreset,
            )
        } ?: NoPresetCard()
    }

    if (state.config.shouldFail == true) {
        ForcedFailureOverlayBanner()
    }
}

@Composable
private fun PopulatedPresets(
    presets: State.Endpoint.Presets,
    onPresetFilterChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    strings: Strings.Widgets.EndpointDetails.Presets = LocalStrings.current.widgets.endpointDetails.presets
) {
    Column {
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = strings.title
        )
        if (presets.allPresets.size > 1) {
            Spacer(Modifier.size(8.dp))
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

    presets.visiblePresets.forEach {
        PresetCard(
            variant = PresetCardVariant.Selectable,
            preset = it,
            onClicked = onDefaultPresetSelected
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

// TODO: Update previews

// @Preview
// @Composable
// private fun PresetsContainerEmptySearchPreview() = PreviewSurface {
// PresetsContainer(
// presets = State.Endpoint.Presets(
// null,
// emptyList(),
// listOf(
// DashboardOverridePreset(
// "",
// "",
// null,
// PartialMockzillaHttpResponse()
// )
// ),
// ""
// ),
// {},
// {},
// {}
// )
// }
// 
// @Preview
// @Composable
// private fun PresetsContainerEmptyPreview() = PreviewSurface {
// PresetsContainer(
// presets = State.Endpoint.Presets(
// appliedPreset = null,
// visiblePresets = emptyList(),
// allPresets = emptyList(),
// filter = ""
// ),
// {},
// {},
// onPresetMoreInfoClicked = {}
// )
// }
