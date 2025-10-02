@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.demo

import android.content.Context
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import kotlinx.serialization.json.Json

private val getMyCow = EndpointConfiguration
    .Builder("cow")
    .setPatternMatcher { uri.endsWith("cow") }
    .configureDashboardOverrides {
        addSuccessPreset(
            MockzillaHttpResponse(
                body = Json.encodeToString(
                    CowDto(
                        name = "ANGRY Bessie",
                        age = 82,
                        likesGrass = false,
                        hasHorns = true,
                        mooSample = "MOOOOOOOO",
                        someValueFromRequest = "",
                    )
                )
            )
        )
    }
    .setDefaultHandler {
        MockzillaHttpResponse(
            body = Json.encodeToString(
                CowDto(
                    name = "Bessie",
                    age = 1,
                    likesGrass = true,
                    hasHorns = false,
                    mooSample = "Mooooooooo",
                    someValueFromRequest = Json.decodeFromString<GetCowRequestDto>(
                        bodyAsString()
                    ).valueInTheRequest
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
