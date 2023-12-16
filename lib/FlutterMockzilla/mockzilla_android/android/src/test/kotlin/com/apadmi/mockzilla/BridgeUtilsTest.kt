package com.apadmi.mockzilla

import BridgeEndpointConfig
import BridgeHttpMethod
import BridgeLogLevel
import BridgeMockzillaConfig
import BridgeMockzillaHttpRequest
import BridgeMockzillaHttpResponse
import BridgeReleaseModeConfig
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
    fun httpMethod_marshalling_returnsExpectedValue() {
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
    fun logLevel_marshalling_returnsExpectedValue() {
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
    fun mockzillaHttpRequest_marshalling_returnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            BridgeMockzillaHttpRequest(
                "https://www.example.com",
                mapOf("Authorization" to "abcd"),
                "body",
                BridgeHttpMethod.PUT,
            ) to
                    MockzillaHttpRequest(
                        "https://www.example.com",
                        mapOf("Authorization" to "abcd"),
                        "body",
                        HttpMethod.Put,
                    )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(BridgeMockzillaHttpRequest.fromNative(native), bridge)
        }
    }

    @Test
    fun mockzillaHttpResponse_marshalling_returnsExpectedValue() {
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
    fun endpointConfig_marshalling_returnsExpectedValue() {
        // Setup
        val endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean = { _: String -> true }
        val defaultHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse =
            { _: String -> MockzillaHttpResponse() }
        val errorHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse =
            { _: String -> MockzillaHttpResponse() }
        val bridgeToNative = mapOf(
            BridgeEndpointConfig(
                "MyEndpoint",
                "my-endpoint",
                50,
                200,
                100,
                BridgeMockzillaHttpResponse(
                    200,
                    mapOf("Content-type" to "application/json"),
                    "body"
                ),
                BridgeMockzillaHttpResponse(
                    400,
                    mapOf("Content-type" to "application/json"),
                    "error"
                )
            ) to
                    EndpointConfiguration(
                        "MyEndpoint",
                        "my-endpoint",
                        50,
                        200,
                        100,
                        { endpointMatcher("my-endpoint") },
                        MockzillaHttpResponse(
                            HttpStatusCode.OK,
                            mapOf("Content-type" to "application/json"),
                            "body"
                        ),
                        MockzillaHttpResponse(
                            HttpStatusCode.BadRequest,
                            mapOf("Content-type" to "application/json"),
                            "error"
                        ),
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
    fun releaseModeConfig_marshalling_returnsExpectedValue() {
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
    fun mockzillaConfig_marshalling_returnsExpectedValue() {
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
                        "name",
                        "key",
                    )
                ),
                isRelease = false,
                localHostOnly = false,
                BridgeLogLevel.INFO,
                BridgeReleaseModeConfig(
                    2000,
                    60_000,
                    86_400_000
                )
            ) to
                    MockzillaConfig(
                        8080,
                        listOf(
                            EndpointConfiguration(
                                "name",
                                "key",
                                null,
                                null,
                                null,
                                { endpointMatcher("key") },
                                null,
                                null,
                                { defaultHandler("key") },
                                { errorHandler("key") }
                            )
                        ),
                        isRelease = false,
                        localhostOnly = false,
                        MockzillaConfig.LogLevel.Info,
                        MockzillaConfig.ReleaseModeConfig(
                            2000,
                            60.seconds,
                            86_400.seconds,
                        ),
                        emptyList()
                    )
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            // TODO: This doesn't pass as lambdas are references, fix this!!
//            assertEquals(
//                bridge.toNative(endpointMatcher, defaultHandler, errorHandler),
//                native
//            )
            assertEquals(
                BridgeMockzillaConfig.fromNative(native),
                bridge,
            )
        }
    }
}