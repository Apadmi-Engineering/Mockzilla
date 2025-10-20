@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.demo

import android.content.Context
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import kotlinx.serialization.json.Json

private val getMyPig = EndpointConfiguration
    .Builder("pig")
    .setPatternMatcher { uri.endsWith("pig") }
    .setDefaultHandler { MockzillaHttpResponse(body = "pigs") }

private val getMySheep = EndpointConfiguration
    .Builder("sheep")
    .setPatternMatcher { uri.endsWith("sheep") }
    .configureDashboardOverrides {
        addPreset(MockzillaHttpResponse(body = """
            This 
            is 
            a
            long
            preset
            about
            sheep
            !
        """.trimIndent()))
        addPreset(
            type = DashboardOverridePreset.Type.Informational,
            description = "A second preset",
            response = MockzillaHttpResponse(body = """
            Another Preset
        """.trimIndent()))
    }
    .setDefaultHandler { MockzillaHttpResponse(body = "sheep") }

private val getMyCow = EndpointConfiguration
    .Builder("cow")
    .setPatternMatcher { uri.endsWith("cow") }
    .configureDashboardOverrides {
        addPreset(
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
        .addEndpoint(getMySheep)
        .addEndpoint(getMyPig)
        .setLogLevel(MockzillaConfig.LogLevel.Verbose)
        .setIsReleaseModeEnabled(isRelease)
        .build(), context
)
