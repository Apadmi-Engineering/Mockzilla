package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import io.ktor.http.HttpStatusCode

@Composable
internal fun EndpointDetailsWidgetPreviewContent() = EndpointDetailsWidgetContent(
    state = EndpointDetailsViewModel.State.Endpoint(
        config = SerializableEndpointConfig.allNulls(
            key = EndpointConfiguration.Key("Key"),
            name = "Endpoint Name",
            versionCode = 1
        ),
        defaultBody = null,
        defaultStatus = null,
        defaultHeaders = null,
        errorBody = null,
        errorStatus = null,
        errorHeaders = null,
        fail = null,
        delayMillis = null,
        jsonEditingDefault = false,
        jsonEditingError = false,
        presets = DashboardOptionsConfig(
            errorPresets = listOf(),
            successPresets = listOf(
                DashboardOverridePreset(
                    name = "Preset",
                    description = "Preset Description",
                    response = MockzillaHttpResponse(
                        statusCode = HttpStatusCode.BadRequest,
                        body = "{ \"name\":\"mockzilla\" }"
                    )
                )
            )
        )
    ),
    onDefaultBodyChange = {},
    onErrorBodyChange = {},
    onFailChange = {},
    onJsonDefaultEditingChange = {},
    onJsonErrorEditingChange = {},
    onDefaultStatusCodeChange = {},
    onErrorStatusCodeChange = {},
    onDelayChange = {},
    onDefaultHeadersChange = {},
    onErrorHeadersChange = {},
    onDefaultPresetSelected = {},
    onErrorPresetSelected = {},
    onResetAll = {}
)
