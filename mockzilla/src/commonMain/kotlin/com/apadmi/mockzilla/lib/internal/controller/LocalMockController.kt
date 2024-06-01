package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.internal.utils.epochMillis
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import co.touchlab.kermit.Logger
import io.ktor.http.*

import kotlinx.coroutines.delay

/**
 * Controller for handling requests from the client application.
 */
internal class LocalMockController(
    private val localCacheService: LocalCacheService,
    private val mockServerMonitor: MockServerMonitor,
    private val endpoints: List<EndpointConfiguration>,
    private val logger: Logger,
) {
    @Suppress("TOO_LONG_FUNCTION")
    suspend fun handleRequest(request: MockzillaHttpRequest): MockzillaHttpResponse {
        val endpoint = endpoints.firstOrNull {
            it.endpointMatcher(request)
        } ?: return MockzillaHttpResponse(
            HttpStatusCode.InternalServerError,
            body = "Could not find endpoint for request ${request.uri}"
        )

        val cachedResponse = localCacheService.getLocalCache(endpoint.key)
        val shouldFail = cachedResponse?.shouldFail ?: endpoint.shouldFail

        // Delay the response for the correct amount of time
        val delay = (cachedResponse?.delayMs ?: endpoint.delay)?.toLong() ?: 0
        delay(delay)

        // Use the cached response by default, i.e. if a user has specified data via the web portal
        // then we return that, otherwise we call the appropriate handler.
        return if (shouldFail) {
            logger.v { "Call to ${endpoint.key} should fail, returning error response" }
            val response = if (listOf(
                cachedResponse?.errorStatus,
                cachedResponse?.errorHeaders,
                cachedResponse?.errorBody
            ).any { it == null }
            ) {
                endpoint.errorHandler(request)
            } else {
                null
            }
            MockzillaHttpResponse(
                statusCode = cachedResponse?.errorStatus ?: response?.statusCode ?: HttpStatusCode.InternalServerError,
                headers = cachedResponse?.errorHeaders ?: response?.headers ?: emptyMap(),
                body = cachedResponse?.errorBody ?: response?.body ?: ""
            )
        } else {
            val response = if (listOf(
                cachedResponse?.defaultStatus,
                cachedResponse?.defaultHeaders,
                cachedResponse?.defaultBody
            ).any { it == null }
            ) {
                endpoint.defaultHandler(request)
            } else {
                null
            }

            MockzillaHttpResponse(
                statusCode = cachedResponse?.defaultStatus ?: response?.statusCode ?: HttpStatusCode.InternalServerError,
                headers = cachedResponse?.defaultHeaders ?: response?.headers ?: emptyMap(),
                body = cachedResponse?.defaultBody ?: response?.body ?: ""
            )
        }.also { response ->
            mockServerMonitor.log(
                epochMillis() - delay,
                delay,
                shouldFail,
                request,
                response
            )
        }
    }
}

internal suspend fun MockServerMonitor.log(
    timeStamp: Long,
    delay: Long,
    isIntendedFailure: Boolean,
    request: MockzillaHttpRequest,
    response: MockzillaHttpResponse,
) = log(
    LogEvent(
        timestamp = timeStamp,
        url = request.uri,
        requestBody = runCatching { request.bodyAsString() }.getOrDefault("<Could not read request body>"),
        requestHeaders = request.headers,
        responseHeaders = response.headers,
        responseBody = response.body,
        status = response.statusCode,
        delay = delay,
        method = request.method.value,
        isIntendedFailure = isIntendedFailure
    )
)
