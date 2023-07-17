package com.apadmi.mockzilla.demo

import android.content.Context
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.service.MockzillaWeb
import com.apadmi.mockzilla.lib.startMockzilla
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

@OptIn(MockzillaWeb::class)
private val getMyCow = EndpointConfiguration
    .Builder("cow")
    .setPatternMatcher { uri.endsWith("cow") }
    .setWebApiDefaultResponse(MockzillaHttpResponse(body = Json.encodeToString(CowDto.empty)))
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                CowDto(
                    name = "Bessie",
                    age = 41,
                    true,
                    hasHorns = false,
                    mooSample = "Mooooooooo",
                    Json.decodeFromString<GetCowRequestDto>(body).aValueInTheRequest
                )
            )
        )
    }

fun startMockServer(context: Context, isRelease: Boolean) = startMockzilla(
    MockzillaConfig.Builder()
        .addEndpoint(getMyCow)
        .setLogLevel(MockzillaConfig.LogLevel.Verbose)
        .setIsReleaseModeEnabled(isRelease)
        .build(), context
)
