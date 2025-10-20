package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse

import io.ktor.http.HttpStatusCode

@Composable
internal fun EndpointDetailsWidgetPreviewContent() = EndpointDetailsWidgetContent(
    state = EndpointDetailsViewModel.State.Endpoint(
        config = SerializableEndpointConfig.allNulls(
            key = EndpointConfiguration.Key("Key"),
            name = "Endpoint Name",
            versionCode = 1
        ),
        fail = null,
        delayMillis = null,
        isLoading = false,
        presets = EndpointDetailsViewModel.State.Endpoint.Presets(
            appliedPreset = null,
            visiblePresets = listOf(
                DashboardOverridePreset(
                    name = "Preset",
                    description = "Preset Description",
                    type = null,
                    response = PartialMockzillaHttpResponse(
                        statusCode = HttpStatusCode.BadRequest,
                        body = "{ \"name\":\"mockzilla\" }"
                    )
                )
            ),
            allPresets = emptyList(),
            filter = "",
        )
    ),
    onDelayChange = {},
    onDefaultPresetSelected = {},
    onResetAll = {},
    onFailChange = {},
    onFilterPresetChanged = {},
    onPresetMoreInfoClicked = {}
)
