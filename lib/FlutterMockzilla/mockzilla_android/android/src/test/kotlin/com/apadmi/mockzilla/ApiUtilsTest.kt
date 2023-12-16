package com.apadmi.mockzilla

import ApiEndpointConfig
import ApiHttpMethod
import ApiLogLevel
import ApiMockzillaConfig
import ApiMockzillaHttpRequest
import ApiMockzillaHttpResponse
import ApiReleaseModeConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class ApiUtilsTest {
    @Test
    fun httpMethod_marshalling_returnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            ApiHttpMethod.GET to HttpMethod.Get,
            ApiHttpMethod.HEAD to HttpMethod.Head,
            ApiHttpMethod.POST to HttpMethod.Post,
            ApiHttpMethod.PUT to HttpMethod.Put,
            ApiHttpMethod.DELETE to HttpMethod.Delete,
            ApiHttpMethod.OPTIONS to HttpMethod.Options,
            ApiHttpMethod.PATCH to HttpMethod.Patch
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(ApiHttpMethod.fromNative(native), bridge)
        }
    }

    @Test
    fun logLevel_marshalling_returnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            ApiLogLevel.DEBUG to MockzillaConfig.LogLevel.Debug,
            ApiLogLevel.ERROR to MockzillaConfig.LogLevel.Error,
            ApiLogLevel.INFO to MockzillaConfig.LogLevel.Info,
            ApiLogLevel.VERBOSE to MockzillaConfig.LogLevel.Verbose,
            ApiLogLevel.WARN to MockzillaConfig.LogLevel.Warn
        )

        // Run test & verify
        bridgeToNative.forEach { (bridge, native) ->
            assertEquals(bridge.toNative(), native)
            assertEquals(ApiLogLevel.fromNative(native), bridge)
        }
    }

    @Test
    fun mockzillaHttpRequest_marshalling_returnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            ApiMockzillaHttpRequest(
                "https://www.example.com",
                mapOf("Authorization" to "abcd"),
                "body",
                ApiHttpMethod.PUT,
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
            assertEquals(ApiMockzillaHttpRequest.fromNative(native), bridge)
        }
    }

    @Test
    fun mockzillaHttpResponse_marshalling_returnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            ApiMockzillaHttpResponse(
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
            assertEquals(ApiMockzillaHttpResponse.fromNative(native), bridge)
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
            ApiEndpointConfig(
                "MyEndpoint",
                "my-endpoint",
                50,
                200,
                100,
                ApiMockzillaHttpResponse(
                    200,
                    mapOf("Content-type" to "application/json"),
                    "body"
                ),
                ApiMockzillaHttpResponse(
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
            assertEquals(ApiEndpointConfig.fromNative(native), bridge)
        }
    }

    @Test
    fun releaseModeConfig_marshalling_returnsExpectedValue() {
        // Setup
        val bridgeToNative = mapOf(
            ApiReleaseModeConfig(
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
            assertEquals(bridge, ApiReleaseModeConfig.fromNative(native))
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
            ApiMockzillaConfig(
                8080L,
                listOf(
                    ApiEndpointConfig(
                        "name",
                        "key",
                    )
                ),
                isRelease = false,
                localHostOnly = false,
                ApiLogLevel.INFO,
                ApiReleaseModeConfig(
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
                ApiMockzillaConfig.fromNative(native),
                bridge,
            )
        }
    }
}