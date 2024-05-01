package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.internal.models.valueOrDefault
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
        val shouldFail = cachedResponse?.shouldFail.valueOrDefault(endpoint.shouldFail)

        // Delay the response for the correct amount of time
        val delay = cachedResponse?.delayMs.valueOrDefault(endpoint.delay ?: 0).toLong()
        delay(delay)

        // Use the cached response by default, i.e. if a user has specified data via the web portal
        // then we return that, otherwise we call the appropriate handler.
        return if (shouldFail) {
            val response = if (listOf(
                cachedResponse?.errorStatus,
                cachedResponse?.headers,
                cachedResponse?.errorBody
            ).all { it is SetOrDont.Set }
            ) {
                null
            } else {
                endpoint.errorHandler(request)
            }
            MockzillaHttpResponse(
                statusCode = cachedResponse?.errorStatus.valueOrDefault(
                    response?.statusCode ?: HttpStatusCode.InternalServerError
                ),
                headers = cachedResponse?.headers.valueOrDefault(response?.headers ?: emptyMap()),
                body = cachedResponse?.errorBody.valueOrDefault(response?.body ?: "")
            )
        } else {
            val response = if (listOf(
                cachedResponse?.defaultStatus,
                cachedResponse?.headers,
                cachedResponse?.defaultBody
            ).all { it is SetOrDont.Set }
            ) {
                null
            } else {
                endpoint.defaultHandler(request)
            }
            MockzillaHttpResponse(
                statusCode = cachedResponse?.defaultStatus.valueOrDefault(
                    response?.statusCode ?: HttpStatusCode.InternalServerError
                ),
                headers = cachedResponse?.headers.valueOrDefault(response?.headers ?: emptyMap()),
                body = cachedResponse?.defaultBody.valueOrDefault(response?.body ?: "")
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
