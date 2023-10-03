package com.apadmi.mockzilla.lib.models

import com.apadmi.mockzilla.lib.service.MockzillaWeb
import io.ktor.http.*

/**
 * @property name
 * @property key
 * @property failureProbability
 * @property delayMean
 * @property delayVariance
 * @property endpointMatcher
 * @property defaultHandler
 * @property errorHandler
 * @property webApiDefaultResponse
 * @property webApiErrorResponse
 */
data class EndpointConfiguration(
    val name: String,
    val key: String,
    val failureProbability: Int? = null,
    val delayMean: Int? = null,
    val delayVariance: Int? = null,
    val endpointMatcher: MockzillaHttpRequest.() -> Boolean,
    val webApiDefaultResponse: MockzillaHttpResponse?,
    val webApiErrorResponse: MockzillaHttpResponse?,
    val defaultHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
    val errorHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
) {
    /**
     * @param id An identifier for this endpoint. Endpoints cannot share an id.
     */
    class Builder(id: String) {
        private var config = EndpointConfiguration(
            name = id,
            key = id,
            endpointMatcher = { uri.endsWith(id) },
            webApiDefaultResponse = null,
            webApiErrorResponse = null,
            defaultHandler = {
                MockzillaHttpResponse(HttpStatusCode.OK)
            }, errorHandler = {
                MockzillaHttpResponse(HttpStatusCode.BadRequest)
            }
        )

        /**
         * Probability of Mockzilla returning a simulated http error for this endpoint. 100 being a
         * guaranteed error .
         *
         * @param percentage (0 -> 100 inclusive)
         */
        fun setFailureProbability(percentage: Int) = apply {
            config = config.copy(failureProbability = percentage)
        }

        /**
         * Used to simulate latency: The artificial mean delay Mockzilla with add to a network request.
         * Used alongside [setMeanDelayMillis] to calculate the actual artificial delay on each invocation.
         *
         * @param delay delay in milliseconds
         */
        fun setMeanDelayMillis(delay: Int) = apply {
            config = config.copy(delayMean = delay)
        }

        /**
         * Used to simulate latency:  The artificial variance in the delay Mockzillaadds to a network
         * request. Used alongside [setMeanDelayMillis] to calculate the actual artificial delay on each
         * invocation. Set this value to 0 to remove any randomness from the delay.
         *
         * @param delay delay in milliseconds
         */
        fun setDelayVarianceMillis(variance: Int) = apply {
            config = config.copy(delayVariance = variance)
        }

        /**
         * The block called when a network request is made to this endpoint. Note: If the value of
         * [setFailureProbability] causes Mockzilla to generate a failure response, then this block
         * will *not* be called, instead the block specified by [setErrorHandler] is called.
         *
         * @param handler
         */
        fun setDefaultHandler(handler: MockzillaHttpRequest.() -> MockzillaHttpResponse) = apply {
            config = config.copy(defaultHandler = handler)
        }

        /**
         * The block called when a network request is made to this endpoint but Mockzilladecides to
         * simulate a server failure.
         *
         * @param handler
         */
        fun setErrorHandler(handler: MockzillaHttpRequest.() -> MockzillaHttpResponse) = apply {
            config = config.copy(errorHandler = handler)
        }

        /**
         * The response which is prefilled in the Mockzilla web page.
         *
         * @param response
         */
        @MockzillaWeb
        fun setWebApiDefaultResponse(response: MockzillaHttpResponse) = apply {
            config = config.copy(webApiDefaultResponse = response)
        }

        /**
         * The error response which is prefilled in the Mockzilla web page.
         *
         * @param response
         */
        @MockzillaWeb
        fun setWebApiErrorResponse(response: MockzillaHttpResponse) = apply {
            config = config.copy(webApiErrorResponse = response)
        }

        /**
         * Specifies whether Mockzilla should map a network request to this endpoint.
         *
         * Used to map an incoming network request to the correct endpoint. The URI is matched against
         * the given regex.
         *
         * This is just a utility wrapper around the more flexible [setPatternMatcher] endpoint.
         *
         * @param regex
         */
        fun setPattern(regex: String) = apply {
            config = config.copy(endpointMatcher = {
                regex.toRegex().matches(uri)
            })
        }

        /**
         * Specifies whether Mockzilla should map a network request to this endpoint.
         *
         * @param matcher Used to map an incoming network request to the correct endpoint.
         */
        fun setPatternMatcher(matcher: MockzillaHttpRequest.() -> Boolean) = apply {
            config = config.copy(endpointMatcher = matcher)
        }

        /**
         * Completes the builder - returns the configuration.
         *
         * @return [EndpointConfiguration]
         */
        fun build() = config
    }
}

/**
 * @property statusCode
 * @property headers
 * @property body
 */
data class MockzillaHttpResponse(
    val statusCode: HttpStatusCode = HttpStatusCode.OK,
    val headers: Map<String, String> = emptyMap(),
    val body: String = "",
)

/**
 * @property headers
 * @property body
 * @property method
 * @property uri
 */
data class MockzillaHttpRequest(
    val uri: String,
    val headers: Map<String, String>,
    val body: String = "",
    val method: HttpMethod,
)
