package com.apadmi.mockzilla.lib.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer

import io.ktor.http.*

import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Configures a single mock endpoint within a Mockzilla server. Defines how incoming requests are
 * matched to this endpoint and what response to return, along with optional dashboard presets and
 * latency simulation.
 *
 * Construct via [Builder].
 *
 * @property name Human-readable display name shown in the management dashboard.
 * @property key Unique identifier for this endpoint, used in management API operations.
 * @property shouldFail Whether this endpoint returns an error response by default. When `true`,
 * [errorHandler] is called instead of [defaultHandler].
 * @property delay Artificial response delay in milliseconds. `null` falls back to the global delay
 * set on [MockzillaConfig.Builder].
 * @property dashboardOptionsConfig Preset responses available to users in the management dashboard.
 * @property versionCode Version number for this endpoint's configuration. Incrementing this value
 * automatically invalidates any cached responses on connected devices.
 * @property endpointMatcher Predicate that determines whether an incoming request should be routed
 * to this endpoint. The first matching endpoint wins.
 * @property defaultHandler Called when a request matches this endpoint and [shouldFail] is `false`.
 * Returns the mock response to send back to the caller.
 * @property errorHandler Called when a request matches this endpoint and [shouldFail] is `true`.
 * Returns the simulated error response to send back to the caller.
 */
public data class EndpointConfiguration(
    val name: String,
    val key: Key,
    val shouldFail: Boolean,
    val delay: Int? = null,
    val dashboardOptionsConfig: DashboardOptionsConfig,
    val versionCode: Int,
    val endpointMatcher: suspend MockzillaHttpRequest.() -> Boolean,
    val defaultHandler: suspend MockzillaHttpRequest.() -> MockzillaHttpResponse,
    val errorHandler: suspend MockzillaHttpRequest.() -> MockzillaHttpResponse,
) {
    /**
     * Unique serializable identifier for an [EndpointConfiguration]. Used to reference endpoints
     * in management API operations such as cache clearing or runtime overrides.
     *
     * @property raw The raw string value of the key.
     */
    @Serializable
    @JvmInline
    public value class Key(public val raw: String)

    /**
     * @param key An identifier for this endpoint. Endpoints cannot share an id.
     */
    public class Builder(key: String) {
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
         * Sets the human readable name of the endpoint (defaults to the value of the `key`)
         *
         * @param name The human-readable display name for this endpoint.
         */
        public fun setName(name: String): Builder = apply {
            config = config.copy(name = name)
        }

        /**
         * Probability of Mockzilla returning a simulated http error for this endpoint. 100 being a
         * guaranteed error .
         *
         * @param percentage (0 -> 100 inclusive)
         */
        @Deprecated("Probabilities are no longer supported", ReplaceWith("setShouldFail(false)"))
        public fun setFailureProbability(percentage: Int): Builder = apply {
            config = config.copy(shouldFail = percentage == 100)
        }

        /**
         * Controls whether calls to this endpoint should fail by default
         */
        public fun setShouldFail(shouldFail: Boolean): Builder = apply {
            config = config.copy(shouldFail = shouldFail)
        }

        /**
         * Used to simulate latency: The artificial mean delay Mockzilla with add to a network request.
         *
         * @param delay delay in milliseconds
         */
        @Deprecated("Delay is now constant with no variance", replaceWith = ReplaceWith("setDelayMillis"))
        public fun setMeanDelayMillis(delay: Int): Builder = apply {
            config = config.copy(delay = delay)
        }

        /**
         * Used to simulate latency: The artificial delay Mockzilla with add to the network request.
         *
         * @param delay delay in milliseconds
         */
        public fun setDelayMillis(delay: Int): Builder = apply {
            config = config.copy(delay = delay)
        }

        /**
         * The block called when a network request is made to this endpoint. Note: If the value of
         * [setShouldFail] causes Mockzilla to generate a failure response, then this block
         * will *not* be called, instead the block specified by [setErrorHandler] is called.
         *
         * @param handler Lambda invoked with the incoming request that returns the mock response.
         */
        public fun setDefaultHandler(handler: suspend MockzillaHttpRequest.() -> MockzillaHttpResponse): Builder = apply {
            config = config.copy(defaultHandler = handler)
        }

        /**
         * The block called when a network request is made to this endpoint but Mockzilla decides to
         * simulate a server failure.
         *
         * @param handler Lambda invoked with the incoming request that returns the simulated error response.
         */
        public fun setErrorHandler(handler: suspend MockzillaHttpRequest.() -> MockzillaHttpResponse): Builder = apply {
            config = config.copy(errorHandler = handler)
        }

        /**
         * Configure the presets that are available to users of the dashboard.
         *
         * @param action Builder block for configuring dashboard presets.
         * @return This builder, for chaining.
         */
        public fun configureDashboardOverrides(
            action: DashboardOptionsConfig.Builder.() -> DashboardOptionsConfig.Builder
        ): Builder = apply {
            config = config.copy(dashboardOptionsConfig = action(DashboardOptionsConfig.Builder()).build())
        }

        /**
         * Sets the version this endpoint is currently set to. A change in the version code will
         * automatically clear any caches on the device associated with this endpoint.
         */
        public fun setVersionCode(code: Int): Builder = apply {
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
         * @param regex The regular expression to match against the full request URI.
         */
        @Suppress("unused")
        public fun setPattern(regex: String): Builder = apply {
            config = config.copy(endpointMatcher = {
                regex.toRegex().matches(uri)
            })
        }

        /**
         * Specifies whether Mockzilla should map a network request to this endpoint.
         *
         * @param matcher Used to map an incoming network request to the correct endpoint.
         */
        public fun setPatternMatcher(matcher: suspend MockzillaHttpRequest.() -> Boolean): Builder = apply {
            config = config.copy(endpointMatcher = matcher)
        }

        /**
         * Completes the builder - returns the configuration.
         *
         * @return [EndpointConfiguration]
         */
        public fun build(): EndpointConfiguration = config
    }
}

/**
 * An HTTP response returned by a mock endpoint handler. Returned from
 * [EndpointConfiguration.Builder.setDefaultHandler] and [EndpointConfiguration.Builder.setErrorHandler]
 * lambdas.
 *
 * @property statusCode The HTTP status code of the response. Defaults to `200 OK`.
 * @property headers HTTP response headers.
 * @property body The response body as a string.
 */
@Serializable
public data class MockzillaHttpResponse(
    @Serializable(with = HttpStatusCodeSerializer::class)
    val statusCode: HttpStatusCode = HttpStatusCode.OK,
    val headers: Map<String, String> = emptyMap(),
    val body: String = "",
) {
    public fun toPartial(): PartialMockzillaHttpResponse = PartialMockzillaHttpResponse(statusCode, headers, body)

    public companion object {
        /**
         * Creates a response with JSON content type and 200 success code for a given body
         *
         * @param body The response body as a string.
         * @return
         */
        public fun successJson(body: String = ""): MockzillaHttpResponse = MockzillaHttpResponse(
            body = body,
            headers = mapOf(HttpHeaders.ContentType to "application/json")
        )
    }
}

/**
 * A partial HTTP response used by dashboard presets, allowing a subset of response fields to
 * be overridden. `null` fields are left unchanged from the endpoint's default response.
 *
 * @property statusCode The HTTP status code override, or `null` to leave unchanged.
 * @property headers HTTP response headers override, or `null` to leave unchanged.
 * @property body The response body override, or `null` to leave unchanged.
 */
@Serializable
public data class PartialMockzillaHttpResponse(
    @Serializable(with = HttpStatusCodeSerializer::class)
    val statusCode: HttpStatusCode? = null,
    val headers: Map<String, String>? = null,
    val body: String? = null
)

public interface MockzillaHttpRequest {
    /**
     * The full uri of the network request
     */
    public val uri: String

    /**
     * Network request's headers
     */
    public val headers: Map<String, String>

    /**
     * Network request method
     */
    public val method: HttpMethod

    /**
     * @return The request body as a ByteArray. Probably only useful for non-string request payload.
     * Most use cases probably should use [bodyAsString]
     * It's safe to call this method multiple times.
     */
    public suspend fun bodyAsBytes(): ByteArray

    /**
     * @return The request body as a string. It's safe to call this method multiple times.
     */
    public suspend fun bodyAsString(): String
}

/**
 * Configures the preset responses available to users in the Mockzilla management dashboard for a
 * specific endpoint. Presets let dashboard users quickly switch between common response scenarios
 * without modifying code.
 *
 * Construct via [Builder] and attach to an endpoint using
 * [EndpointConfiguration.Builder.configureDashboardOverrides].
 * @property errorPresets
 * @property successPresets
 */
@Suppress("KDOC_NO_CONSTRUCTOR_PROPERTY_WITH_COMMENT")
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DashboardOptionsConfig(
    @Deprecated("Error Presets will be removed in a future version")
    // No longer used, still included for backward compatibility
    val errorPresets: List<DashboardOverridePreset>,
    // This will be renamed to just "presets" in a future release. This gets management api ready to consume that change
    @Deprecated("Deprecated", replaceWith = ReplaceWith("presets"))
    @JsonNames("presets")
    val successPresets: List<DashboardOverridePreset>
) {
    /**
     * The list of preset responses available in the dashboard for this endpoint.
     */
    public val presets: List<DashboardOverridePreset> get() = successPresets

    public class Builder {
        private val presets = mutableListOf<DashboardOverridePreset>()

        /**
         * Adds a preset response option to the dashboard for this endpoint.
         *
         * @param response The full response this preset applies when selected.
         * @param name Display name for this preset in the dashboard. Defaults to "Preset N".
         * @param description Optional description shown alongside the preset.
         * @param type Visual classification for this preset. Defaults to a type inferred from the
         * response status code when `null`.
         * @return This builder, for chaining.
         */
        public fun addPreset(
            response: MockzillaHttpResponse,
            name: String? = null,
            description: String? = null,
            type: DashboardOverridePreset.Type? = null
        ): Builder = addPreset(response.toPartial(), name, description, type)

        /**
         * Adds a partial preset response option to the dashboard for this endpoint. Only fields
         * set on [response] are overridden; `null` fields retain the endpoint's default values.
         *
         * @param response The partial response this preset applies when selected.
         * @param name Display name for this preset in the dashboard. Defaults to "Preset N".
         * @param description Optional description shown alongside the preset.
         * @param type Visual classification for this preset. Defaults to a type inferred from the
         * response status code when `null`.
         * @return This builder, for chaining.
         */
        public fun addPreset(
            response: PartialMockzillaHttpResponse,
            name: String? = null,
            description: String? = null,
            type: DashboardOverridePreset.Type? = null
        ): Builder = presets.add(
            DashboardOverridePreset(
                name ?: "Preset ${presets.count() + 1}",
                description,
                type,
                response
            )
        ).let { this }

        @Deprecated(
            "Separate success/error presets are no longer supported",
            replaceWith = ReplaceWith("addPreset"))
        public fun addSuccessPreset(
            response: MockzillaHttpResponse,
            name: String? = null,
            description: String? = null
        ): Builder = addPreset(response, name, description)

        @Deprecated(
            "Separate success/error presets are no longer supported",
            replaceWith = ReplaceWith("addPreset"))
        public fun addErrorPreset(
            response: MockzillaHttpResponse,
            name: String? = null,
            description: String? = null
        ): Builder = addPreset(response, name, description)

        public fun build(): DashboardOptionsConfig = DashboardOptionsConfig(errorPresets = emptyList(), presets)
    }
}

/**
 * A named response configuration that can be applied to an endpoint from the Mockzilla management
 * dashboard, overriding the endpoint's default or error response for a session.
 *
 * @property name Display name shown in the dashboard preset list.
 * @property description Optional description shown alongside the preset in the dashboard.
 * @property type Visual classification for the preset in the dashboard. Defaults to a type
 * inferred from the response status code when `null`.
 * @property response The partial response this preset applies when selected.
 * @property isManagementUiDefinedCustomPreset `true` when this preset was created interactively
 * by a user in the management dashboard, as opposed to being defined in code via
 * [EndpointConfiguration.Builder.configureDashboardOverrides].
 */
@Serializable
public data class DashboardOverridePreset(
    val name: String,
    val description: String?,
    val type: Type?,
    val response: PartialMockzillaHttpResponse,
    val isManagementUiDefinedCustomPreset: Boolean = false
) {
    /**
     * Visual classification for a [DashboardOverridePreset] in the management dashboard. Used to
     * display presets with appropriate styling.
     */
    @Serializable
    public enum class Type {
        ClientError,
        Informational,
        Other,
        Redirect,
        ServerError,
        Success,
        ;

        public companion object {
            public fun fromString(str: String): Type? = when (str) {
                "ClientError" -> ClientError
                "Informational" -> Informational
                "Other" -> Other
                "Redirect" -> Redirect
                "ServerError" -> ServerError
                "Success" -> Success
                else -> null
            }
        }
    }
}
