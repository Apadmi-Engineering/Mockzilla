package com.apadmi.mockzilla.mock

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.service.MockzillaWeb

import io.ktor.server.application.hooks.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(MockzillaWeb::class)
private val getMyCow = EndpointConfiguration
    .Builder("cow")
    .setPatternMatcher { uri.endsWith("cow") }
    .setWebApiDefaultResponse(
        MockzillaHttpResponse(
            body = Json.encodeToString(CowDto.empty),
            headers = mapOf("Content-Type" to "application/json")
        )
    )
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                CowDto(
                    name = "Bessie",
                    age = 1,
                    true,
                    hasHorns = false,
                    mooSample = "Mooooooooo",
                    Json.decodeFromString<GetCowRequestDto>(body).aValueInTheRequest
                )
            ), headers = mapOf("Content-Type" to "application/json")
        )
    }

val mockzillaConfig = MockzillaConfig.Builder()
    .addEndpoint(getMyCow)
    .setLogLevel(MockzillaConfig.LogLevel.Verbose)
    .build()
