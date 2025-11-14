package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse

import io.ktor.http.HttpStatusCode

internal val mockPresets = listOf(
    DashboardOverridePreset(
        name = "Preset",
        description = "Preset Description",
        type = null,
        response = PartialMockzillaHttpResponse(
            statusCode = HttpStatusCode.BadRequest,
            body = "{ \"name\":\"mockzilla\" }"
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
    state: EndpointDetailsViewModel.State
) = EndpointDetailsWidgetContent(
    state = state,
    onDelayChange = {},
    onDefaultPresetSelected = {},
    onResetAll = {},
    onFailChange = {},
    onFilterPresetChanged = {},
    onPresetMoreInfoClicked = {},
    onCreatePreset = {},
    onEditPreset = {},
)

@Suppress("TOO_LONG_FUNCTION")
internal fun endpointDetailsWidgetSuccessState(
    fail: Boolean = false
) = EndpointDetailsViewModel.State.Endpoint(
    config = SerializableEndpointConfig.allNulls(
        key = EndpointConfiguration.Key("Key"),
        name = "Endpoint Name",
        versionCode = 1
    ).copy(shouldFail = fail),
    fail = fail,
    delayMillis = null,
    isLoading = false,
    presets = EndpointDetailsViewModel.State.Endpoint.Presets(
        appliedPreset = null,
        visiblePresets = mockPresets,
        allPresets = mockPresets,
        filter = "",
    )
)
