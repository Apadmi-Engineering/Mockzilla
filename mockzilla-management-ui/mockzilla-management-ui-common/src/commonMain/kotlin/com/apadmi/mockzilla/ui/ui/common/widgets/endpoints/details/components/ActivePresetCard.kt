package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.NoPresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCard
import com.apadmi.mockzilla.ui.ui.common.components.PresetCardVariant
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.endpointDetailsWidgetSuccessState
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.mockPresets
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ActivePresetCard(
    modifier: Modifier = Modifier,
    state: State.Endpoint,
    onEditPreset: (DashboardOverridePreset) -> Unit,
    onCreateNewPreset: () -> Unit,
    strings: Strings.Widgets.EndpointDetails = LocalStrings.current.widgets.endpointDetails
) = Box(
    modifier = modifier
        .animateContentSize()
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
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = strings.presets.activePresetTitle,
                style = MaterialTheme.typography.titleMedium
            )
            CustomOutlineButton(
                label = strings.presets.createCustomButton,
                leadingIcon = Icons.Outlined.AddCircle,
                onClick = onCreateNewPreset,
                variant = OutlineButtonVariant.Primary
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

@Preview
@Composable
private fun ActivePresetCardPreview() = PreviewSurface {
    ActivePresetCardPreviewContainer()
}

@Preview
@Composable
private fun ActivePresetCardDarkPreview() = PreviewSurface(darkTheme = true) {
    ActivePresetCardPreviewContainer()
}

@Preview
@Composable
private fun ActivePresetCardForcedFailurePreview() = PreviewSurface {
    ActivePresetCardPreviewContainer(fail = true)
}

@Preview
@Composable
private fun ActivePresetCardForcedFailureDarkPreview() = PreviewSurface(darkTheme = true) {
    ActivePresetCardPreviewContainer(fail = true)
}

@Composable
private fun ActivePresetCardPreviewContainer(
    fail: Boolean = false
) = PreviewSurface {
    ActivePresetCard(
        state = endpointDetailsWidgetSuccessState(fail = fail).copy(
            presets = endpointDetailsWidgetSuccessState().presets.copy(
                appliedPreset = mockPresets.first()
            )
        ),
        onEditPreset = {},
        onCreateNewPreset = {}
    )
}
