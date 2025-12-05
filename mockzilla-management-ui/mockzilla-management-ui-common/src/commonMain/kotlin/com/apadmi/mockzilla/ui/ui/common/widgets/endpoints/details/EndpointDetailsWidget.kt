@file:Suppress("MAGIC_NUMBER", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBanner
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.components.SurfaceHeader
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.*
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components.ActivePresetCard
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.components.PresetsContainer

import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

@Composable
private fun ColumnScope.PopulatedState(
    state: State.Endpoint,
    strings: Strings,
    onResetAll: () -> Unit,
    onFailChange: (Boolean?) -> Unit,
    onDelayChange: (Int?) -> Unit,
    onFilterPresetChanged: (String) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onCreatePreset: () -> Unit,
    onEditPreset: () -> Unit,
) {
    Box {
        SurfaceHeader(
            title = state.config.name,
            subtitle = strings.widgets.endpointDetails.subtitle,
        ) {
            CustomOutlineButton(
                label = strings.widgets.globalControls.resetAllLabel,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                variant = OutlineButtonVariant.Secondary,
                onClick = onResetAll
            )
        }

        Box(Modifier.height(12.dp).fillMaxWidth().clipToBounds()) {
            TogglableProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isLoading,
                trackColor = Color.Transparent
            )
        }
    }

    ForceFailureBanner(
        modifier = Modifier.padding(horizontal = 12.dp),
        state = if (state.config.shouldFail == true) {
            ForceFailureBannerState.FullFailure
        } else {
            ForceFailureBannerState.Normal
        },
        onRestoreApiClicked = { onFailChange(false) },
        onForceFailureClicked = { onFailChange(true) },
    )

    ResponseLatencyCard(
        modifier = Modifier.padding(horizontal = 12.dp),
        initialValue = state.config.delayMs,
        onChange = onDelayChange,
        onReset = { onDelayChange(null) }
    )

    ActivePresetCard(
        modifier = Modifier.padding(horizontal = 12.dp),
        state = state,
        onEditPreset = { onEditPreset() },
        onCreateNewPreset = onCreatePreset
    )

    PresetsContainer(
        modifier = Modifier.padding(horizontal = 12.dp),
        state = state,
        onPresetFilterChanged = onFilterPresetChanged,
        onDefaultPresetSelected = onDefaultPresetSelected,
        onPresetMoreInfoClicked = onPresetMoreInfoClicked
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun EndpointDetailsWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key?,
    onCreatePreset: (EndpointConfiguration.Key) -> Unit,
    onEditPreset: (EndpointConfiguration.Key) -> Unit
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
        onCreatePreset = { activeEndpoint?.let { onCreatePreset(activeEndpoint) } },
        onEditPreset = { activeEndpoint?.let { onEditPreset(activeEndpoint) } },
        onPresetMoreInfoClicked = {
            // TODO, Add preset docs and update link
            uriHandler.openUri("https://mockzilla.apadmi.dev/")
        }
    )
}

@Composable
fun EndpointDetailsWidgetContent(
    state: State,
    onDelayChange: (Int?) -> Unit,
    onFailChange: (Boolean?) -> Unit,
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onResetAll: () -> Unit,
    onFilterPresetChanged: (String) -> Unit,
    onPresetMoreInfoClicked: () -> Unit,
    onCreatePreset: () -> Unit,
    onEditPreset: () -> Unit,
    strings: Strings = LocalStrings.current,
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
        .background(color = MaterialTheme.colorScheme.background),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    when (state) {
        is State.Empty -> EmptyState(
            title = strings.widgets.endpointDetails.emptyTitle,
            description = strings.widgets.endpointDetails.emptyDescription
        )

        is State.Endpoint -> PopulatedState(
            state,
            strings,
            onResetAll,
            onFailChange,
            onDelayChange,
            onFilterPresetChanged,
            onDefaultPresetSelected,
            onPresetMoreInfoClicked,
            onCreatePreset,
            onEditPreset
        )
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
