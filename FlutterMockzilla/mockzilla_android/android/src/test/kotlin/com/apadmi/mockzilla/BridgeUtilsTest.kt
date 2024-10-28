package com.apadmi.mockzilla

import BridgeDashboardOptionsConfig
import BridgeDashboardOverridePreset
import BridgeEndpointConfig
import BridgeHttpMethod
import BridgeLogLevel
import BridgeMockzillaConfig
import BridgeMockzillaHttpResponse
import BridgeReleaseModeConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class BridgeUtilsTest {
    @Test
    fun httpMethodMarshallingReturnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeHttpMethod.GET to HttpMethod.Get,
            BridgeHttpMethod.HEAD to HttpMethod.Head,
            BridgeHttpMethod.POST to HttpMethod.Post,
            BridgeHttpMethod.PUT to HttpMethod.Put,
            BridgeHttpMethod.DELETE to HttpMethod.Delete,
            BridgeHttpMethod.OPTIONS to HttpMethod.Options,
            BridgeHttpMethod.PATCH to HttpMethod.Patch
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(BridgeHttpMethod.fromNative(native), bridge)
        }
    }

    @Test
    fun logLevelMarshallingReturnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeLogLevel.DEBUG to MockzillaConfig.LogLevel.Debug,
            BridgeLogLevel.ERROR to MockzillaConfig.LogLevel.Error,
            BridgeLogLevel.INFO to MockzillaConfig.LogLevel.Info,
            BridgeLogLevel.VERBOSE to MockzillaConfig.LogLevel.Verbose,
            BridgeLogLevel.WARN to MockzillaConfig.LogLevel.Warn,
            BridgeLogLevel.ASSERTION to MockzillaConfig.LogLevel.Assert,
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(BridgeLogLevel.fromNative(native), bridge)
        }
    }

    @Test
    fun mockzillaHttpResponseMarshallingReturnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeMockzillaHttpResponse(
                200,
                mapOf("Content-type" to "application/json"),
                "body"
            ) to
                    MockzillaHttpResponse(
                        HttpStatusCode.OK,
                        mapOf("Content-type" to "application/json"),
                        "body"
                    )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(BridgeMockzillaHttpResponse.fromNative(native), bridge)
        }
    }

    @Test
    fun dashboardOverridePresetMarshallingReturnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeDashboardOverridePreset(
                "Default response",
                null,
                BridgeMockzillaHttpResponse(
                    200,
                    emptyMap(),
                    "",
                )
            ) to DashboardOverridePreset(
                "Default response",
                null,
                MockzillaHttpResponse(
                    HttpStatusCode.OK,
                    emptyMap(),
                    ""
                )
            ),
            BridgeDashboardOverridePreset(
                "Error response",
                "Unauthorized response",
                BridgeMockzillaHttpResponse(
                    401,
                    emptyMap(),
                    ""
                )
            ) to DashboardOverridePreset(
                "Error response",
                "Unauthorized response",
                MockzillaHttpResponse(
                    HttpStatusCode.Unauthorized,
                    emptyMap(),
                    ""
                )
            )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(BridgeDashboardOverridePreset.fromNative(native), bridge)
        }
    }

    @Test
    fun dashboardOptionsConfigMarshallingReturnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeDashboardOptionsConfig(
                emptyList(), emptyList()
            ) to DashboardOptionsConfig(
                emptyList(), emptyList()
            ),
            BridgeDashboardOptionsConfig(
                listOf(
                    BridgeDashboardOverridePreset(
                        "Default response",
                        null,
                        BridgeMockzillaHttpResponse(200, emptyMap(), "")
                    )
                ),
                listOf(
                    BridgeDashboardOverridePreset(
                        "Error response",
                        null,
                        BridgeMockzillaHttpResponse(500, emptyMap(), "")
                    )
                )
            ) to DashboardOptionsConfig(
                successPresets = listOf(
                    DashboardOverridePreset(
                        "Default response",
                        null,
                        MockzillaHttpResponse(HttpStatusCode.OK, emptyMap(), "")
                    )
                ),
                errorPresets = listOf(
                    DashboardOverridePreset(
                        "Error response",
                        null,
                        MockzillaHttpResponse(HttpStatusCode.InternalServerError, emptyMap(), "")
                    )
                )
            )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(BridgeDashboardOptionsConfig.fromNative(native), bridge)
        }
    }

    @Test
    fun endpointConfigMarshallingReturnsExpectedValue() {
        // Setup
        val endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean = { _: String -> true }
        val defaultHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse =
            { _: String -> MockzillaHttpResponse() }
        val errorHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse =
            { _: String -> MockzillaHttpResponse() }
        val bridgeToNative = mapOf(
            BridgeEndpointConfig(
                "MyEndpoint", "my-endpoint", false, 200, 1,
                BridgeDashboardOptionsConfig(emptyList(), emptyList())
            ) to
                    EndpointConfiguration(
                        "MyEndpoint", EndpointConfiguration.Key("my-endpoint"), false, 200,
                        DashboardOptionsConfig(emptyList(), emptyList()),
                        1,
                        { endpointMatcher("my-endpoint") },
                        { defaultHandler("my-endpoint") },
                        { errorHandler("my-endpoint") },
                    )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            // TODO: This doesn't pass as lambdas are references, fix this!!
            // assertEquals(bridge.toNative(endpointMatcher, defaultHandler, errorHandler), native)
            assertEquals(BridgeEndpointConfig.fromNative(native), bridge)
        }
    }

    @Test
    fun releaseModeConfigMarshallingReturnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeReleaseModeConfig(
                2000,
                60_000,
                86_400_000
            ) to
                    MockzillaConfig.ReleaseModeConfig(
                        2000,
                        Duration.parseIsoString("PT1M"),
                        Duration.parseIsoString("P1D"),
                    )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(native, bridge.toNative())
            assertEquals(bridge, BridgeReleaseModeConfig.fromNative(native))
        }
    }

    @Test
    fun mockzillaConfigMarshallingReturnsExpectedValue() {
        // Setup
        val endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean = { _: String -> true }
        val defaultHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse =
            { _: String -> MockzillaHttpResponse() }
        val errorHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse =
            { _: String -> MockzillaHttpResponse() }
        val bridgeToNative = mapOf(
            BridgeMockzillaConfig(
                8080L,
                listOf(
                    BridgeEndpointConfig(
                        "name", "key", false, 100, 1,
                        BridgeDashboardOptionsConfig(
                            emptyList(), emptyList()
                        ),
                    )
                ),
                isRelease = false, localHostOnly = false, BridgeLogLevel.INFO,
                BridgeReleaseModeConfig(2000, 60_000, 86_400_000),
                false
            ) to MockzillaConfig(
                8080,
                listOf(
                    EndpointConfiguration(
                        "name",
                        EndpointConfiguration.Key("key"),
                        false,
                        null,
                        DashboardOptionsConfig(
                            emptyList(), emptyList()
                        ),
                        1,
                        { endpointMatcher("key") },
                        { defaultHandler("key") },
                        { errorHandler("key") }
                    )
                ),
                isRelease = false,
                localhostOnly = false,
                MockzillaConfig.LogLevel.Info,
                MockzillaConfig.ReleaseModeConfig(2000, 60.seconds, 86_400.seconds),
                false,
                emptyList()
            )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            // TODO: This doesn't pass as lambdas are references, fix this!!
            // assertEquals(
            // bridge.toNative(endpointMatcher, defaultHandler, errorHandler),native
            // )
            assertEquals(BridgeMockzillaConfig.fromNative(native), bridge)
        }
    }
}
