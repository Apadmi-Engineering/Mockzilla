package com.apadmi.mockzilla.lib.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
import com.apadmi.mockzilla.lib.service.MockzillaWeb

import io.ktor.http.*
import io.ktor.server.request.ApplicationRequest

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * @property name
 * @property key
 * @property shouldFail
 * @property delay
 * @property endpointMatcher
 * @property versionCode
 * @property defaultHandler
 * @property errorHandler
 * @property dashboardOptionsConfig
 */
data class EndpointConfiguration(
    val name: String,
    val key: Key,
    val shouldFail: Boolean,
    val delay: Int? = null,
    val dashboardOptionsConfig: DashboardOptionsConfig,
    val versionCode: Int,
    val endpointMatcher: MockzillaHttpRequest.() -> Boolean,
    val defaultHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
    val errorHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
) {
    /**
     * @property raw
     */
    @Serializable
    @JvmInline
    value class Key(val raw: String)

    /**
     * @param key An identifier for this endpoint. Endpoints cannot share an id.
     */
    class Builder(key: String) {
        private var config = EndpointConfiguration(
            name = key,
            key = Key(key),
            endpointMatcher = { uri.endsWith(key) },
            shouldFail = false,
            dashboardOptionsConfig = DashboardOptionsConfig(emptyList(), emptyList()),
            versionCode = Int.MIN_VALUE,
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
        @Deprecated("Probabilities are no longer supported", ReplaceWith("setShouldFail(false)"))
        fun setFailureProbability(percentage: Int) = apply {
            config = config.copy(shouldFail = percentage == 100)
        }

        /**
         * Controls whether calls to this endpoint should fail by default
         */
        fun setShouldFail(shouldFail: Boolean) = apply {
            config = config.copy(shouldFail = shouldFail)
        }

        /**
         * Used to simulate latency: The artificial mean delay Mockzilla with add to a network request.
         * Used alongside [setMeanDelayMillis] to calculate the actual artificial delay on each invocation.
         *
         * @param delay delay in milliseconds
         */
        fun setMeanDelayMillis(delay: Int) = apply {
            config = config.copy(delay = delay)
        }

        /**
         * No longer supported
         *
         * @param delay delay in milliseconds
         */
        @Deprecated("No longer supported")
        fun setDelayVarianceMillis(variance: Int) = apply {
            // No-op
        }

        /**
         * The block called when a network request is made to this endpoint. Note: If the value of
         * [setShouldFail] causes Mockzilla to generate a failure response, then this block
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
        @Deprecated(
            "Obsolete, see `configureDashboardOverrides`",
            replaceWith = ReplaceWith("configureDashboardOverrides")
        )
        fun setWebApiDefaultResponse(response: MockzillaHttpResponse) = this

        /**
         * The error response which is prefilled in the Mockzilla web page.
         *
         * @param response
         */
        @MockzillaWeb
        @Deprecated(
            "Obsolete, see `configureDashboardOverrides`",
            replaceWith = ReplaceWith("configureDashboardOverrides")
        )
        fun setWebApiErrorResponse(response: MockzillaHttpResponse) = this

        /**
         * Configure the presets that are available to users of the dashboard.
         *
         * @param action
         * @return
         */
        fun configureDashboardOverrides(
            action: DashboardOptionsConfig.Builder.() -> DashboardOptionsConfig.Builder
        ) = apply {
            config = config.copy(dashboardOptionsConfig = action(DashboardOptionsConfig.Builder()).build())
        }

        /**
         * Sets the version this endpoint is currently set to. A change in the version code will
         * automatically clear any caches on the device associated with this endpoint.
         */
        fun setVersionCode(code: Int) = apply {
            config = config.copy(versionCode = code)
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
@Serializable
data class MockzillaHttpResponse(
    @Serializable(with = HttpStatusCodeSerializer::class)
    val statusCode: HttpStatusCode = HttpStatusCode.OK,
    val headers: Map<String, String> = emptyMap(),
    val body: String = "",
)

interface MockzillaHttpRequest {
    /**
     * The full uri of the network request
     */
    val uri: String

    /**
     * Network request's headers
     */
    val headers: Map<String, String>

    /**
     * Network request method
     */
    val method: HttpMethod

    /**
     * The string representation of the request body
     */
    @Deprecated("`body`is deprecated", replaceWith = ReplaceWith("bodyAsString()"))
    val body: String

    /**
     * The underlying ktor [ApplicationRequest](https://api.ktor.io/ktor-server/ktor-server-core/io.ktor.server.request/-application-request/index.html).
     */
    val underlyingKtorRequest: ApplicationRequest

    /**
     * @return The request body as a ByteArray. Probably only useful for non-string request payload.
     * Most use cases probably should use [bodyAsString]
     * It's safe to call this method multiple times.
     */
    fun bodyAsBytes(): ByteArray

    /**
     * @return The request body as a string. It's safe to call this method multiple times.
     */
    fun bodyAsString(): String
}

/**
 * @property errorPresets
 * @property successPresets
 */
@Serializable
data class DashboardOptionsConfig(
    val errorPresets: List<DashboardOverridePreset>,
    val successPresets: List<DashboardOverridePreset>
) {
    class Builder {
        private val errorPresets = mutableListOf<DashboardOverridePreset>()
        private val successPresets = mutableListOf<DashboardOverridePreset>()

        fun addSuccessPreset(
            response: MockzillaHttpResponse,
            name: String? = null,
            description: String? = null
        ) = successPresets.add(
            DashboardOverridePreset(
                name ?: "Preset ${successPresets.count() + 1}",
                description,
                response
            )
        ).let { this }

        fun addErrorPreset(
            response: MockzillaHttpResponse,
            name: String? = null,
            description: String? = null
        ) = errorPresets.add(
            DashboardOverridePreset(
                name ?: "Error Preset ${errorPresets.count() + 1}",
                description,
                response
            )
        ).let { this }

        fun build() = DashboardOptionsConfig(
            errorPresets = errorPresets,
            successPresets = successPresets
        )
    }
}

/**
 * @property name
 * @property description
 * @property response
 */
@Serializable
data class DashboardOverridePreset(
    val name: String,
    val description: String?,
    val response: MockzillaHttpResponse
)
