package com.apadmi.mockzilla.mock

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import io.ktor.server.application.hooks.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val port = 8085

private val getMyCow = EndpointConfiguration
    .Builder("cow")
    .setPatternMatcher { uri.endsWith("cow") }
    .configureDashboardOverrides {
        addSuccessPreset(
            MockzillaHttpResponse(
                body = Json.encodeToString(CowDto.empty),
                headers = mapOf("Content-Type" to "application/json")
            ), name = "Cow preset 1", description = "A simple cow preset"
        )
        addSuccessPreset(
            MockzillaHttpResponse(
                body = Json.encodeToString(CowDto.empty.copy(mooSample = "A VERY BIG MOO")),
                headers = mapOf("Content-Type" to "application/json")
            ), name = "Cow preset 2", description = "A cow preset with a big moo"
        )
        addErrorPreset(
            MockzillaHttpResponse(
                body = "Moooo something's gone mooosively wrong",
            ), description = "A cow error"
        )
    }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                CowDto(
                    name = "Bessie",
                    age = 1,
                    true,
                    hasHorns = false,
                    mooSample = "Mooooooooo",
                    Json.decodeFromString<GetCowRequestDto>(body).valueInTheRequest
                )
            ), headers = mapOf("Content-Type" to "application/json")
        )
    }

private val getMySheep = EndpointConfiguration
    .Builder("sheep")
    .setPatternMatcher { uri.endsWith("sheep") }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(SheepDto("Kevan", "BAAAAH")),
            headers = mapOf("Content-Type" to "application/json")
        )
    }

val mockzillaConfig = MockzillaConfig.Builder()
    .addEndpoint(getMyCow)
    .addEndpoint(getMySheep)
    .setPort(port)
    .setLogLevel(MockzillaConfig.LogLevel.Verbose)
    .build()
