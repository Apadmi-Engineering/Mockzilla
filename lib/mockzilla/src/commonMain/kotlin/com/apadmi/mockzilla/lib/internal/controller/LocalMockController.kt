package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.service.DelayAndFailureDecision
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
    private val delayAndFailureDecision: DelayAndFailureDecision,
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
        val globalOverrides = localCacheService.getGlobalOverrides()
        val cachedResponse = localCacheService.getLocalCache(endpoint.key)

        val shouldFail = delayAndFailureDecision.shouldFail(
            globalOverrides?.failProbability ?: cachedResponse?.failProbability ?: endpoint.failureProbability ?: 0
        )

        val computedDelay = delayAndFailureDecision.generateDelayMillis(
            globalOverrides?.delayMean ?: cachedResponse?.delayMean ?: endpoint.delayMean ?: 0,
            globalOverrides?.delayVariance ?: cachedResponse?.delayVariance
                ?: endpoint.delayVariance ?: 0
        )
        logger.d {
            "Endpoint = ${endpoint.key}: Responding with a delay: $computedDelay, ${
                "Mockzilla will fail this request".takeIf { shouldFail }
            }"
        }
        cachedResponse?.let {
            logger.d {
                "Endpoint = ${endpoint.key}: Cached response found - returning cache $cachedResponse"
            }
        } ?: logger.d {
            "Endpoint = ${endpoint.key}: No cache response found, calling request handler"
        }

        // Delay the response for the correct amount of time
        delay(computedDelay)

        // Use the cached response by default, i.e. if a user has specified data via the web portal
        // then we return that, otherwise we call the appropriate handler.
        return if (shouldFail) {
            cachedResponse?.toErrorMockzillaResponse() ?: endpoint.errorHandler(request)
        } else {
            cachedResponse?.toDefaultMockzillaResponse() ?: endpoint.defaultHandler(request)
        }.also { response ->
            mockServerMonitor.log(
                epochMillis() - computedDelay,
                computedDelay,
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
) = log(LogEvent(
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
))
