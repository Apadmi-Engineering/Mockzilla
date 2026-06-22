package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.State.Endpoint.Presets
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity

import io.ktor.http.HttpStatusCode

internal val mockPresets = listOf(
    DashboardOverridePreset(
        name = "Preset",
        description = "Preset Description",
        type = null,
        response = PartialMockzillaHttpResponse(
            statusCode = HttpStatusCode.BadRequest,
            body = "{ \"name\":\"mockzilla\" }",
        )
    ),
    DashboardOverridePreset(
        name = "Preset 2",
        description = "Preset Description",
        type = DashboardOverridePreset.Type.Success,
        response = PartialMockzillaHttpResponse(
            statusCode = HttpStatusCode.OK,
            body = "{ \"name\":\"mockzilla\" }"
        )
    )
)

@Composable
internal fun EndpointDetailsWidgetPreviewContent(
    state: State
) = EndpointDetailsWidgetContent(
    state = state,
    onDelayChange = {},
    onDefaultPresetSelected = {},
    onResetAll = {},
    onFailChange = {},
    onFilterPresetChanged = {},
    onRowDensityChanged = {},
    onPresetMoreInfoClicked = {},
    onCreatePreset = {},
)

@Suppress("TOO_LONG_FUNCTION")
internal fun endpointDetailsWidgetSuccessState(
    fail: Boolean = false
) = State.Endpoint(
    config = SerializableEndpointConfig.allNulls(
        key = EndpointConfiguration.Key("Key"),
        name = "Endpoint Name",
        versionCode = 1
    ).copy(shouldFail = fail),
    fail = fail,
    delayMillis = null,
    isLoading = false,
    layoutMode = RowDensity.Compact,
    presets = Presets(
        appliedPreset = null,
        visiblePresets = mockPresets,
        allPresets = mockPresets,
        filter = "",
    )
)
