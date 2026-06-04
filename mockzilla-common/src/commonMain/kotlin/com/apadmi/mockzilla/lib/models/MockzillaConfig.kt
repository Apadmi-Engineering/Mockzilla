package com.apadmi.mockzilla.lib.models

import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import com.apadmi.mockzilla.lib.service.MockzillaLogWriter

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Top-level configuration for a Mockzilla server instance. All properties are set via
 * [MockzillaConfig.Builder].
 *
 * @property port The port the server binds to. `0` causes the OS to assign an available port.
 * @property endpoints The mock endpoints registered on this server.
 * @property isRelease When `true`, activates release mode: rate limiting, token authentication,
 * and localhost-only restrictions are applied. See [ReleaseModeConfig] for details.
 * @property localhostOnly When `true`, the server only accepts connections from `127.0.0.1`,
 * blocking the management desktop interface and other external tools.
 * @property logLevel Verbosity of Mockzilla's internal logging.
 * @property releaseModeConfig Rate limiting and authentication config applied in release mode.
 * @property isNetworkDiscoveryEnabled When `true`, Mockzilla broadcasts itself via ZeroConf
 * (Bonjour) so the management desktop can discover it. Always disabled in release mode.
 * @property additionalLogWriters Extra log sinks in addition to standard output.
 */
data class MockzillaConfig(
    val port: Int,
    val endpoints: List<EndpointConfiguration>,
    val isRelease: Boolean,
    val localhostOnly: Boolean,
    val logLevel: LogLevel,
    val releaseModeConfig: ReleaseModeConfig,
    val isNetworkDiscoveryEnabled: Boolean,
    val additionalLogWriters: List<MockzillaLogWriter>
) {
    /**
     * Defines the verbosity of Mockzilla's internal logging.
     */
    enum class LogLevel {
        Assert,
        Debug,
        Error,
        Info,
        Verbose,
        Warn,
        ;
    }

    /**
     * Rate Limiting: Uses Ktor's rate limiting, params described here: https://ktor.io/docs/rate-limit.html#configure-rate-limiting
     * Each network call will require a token with the provided lifespan
     *
     * @property rateLimit
     * @property rateLimitRefillPeriod
     * @property tokenLifeSpan
     */
    data class ReleaseModeConfig(
        val rateLimit: Int = 60,
        val rateLimitRefillPeriod: Duration = 60.seconds,
        val tokenLifeSpan: Duration = 0.5.seconds
    )

    class Builder {
        private var logLevel: LogLevel = LogLevel.Info
        private var port = defaultPort
        private var endpoints: MutableList<EndpointConfiguration> = mutableListOf()
        private var delay = 100
        private var isRelease = false
        private var releaseConfig: ReleaseModeConfig = ReleaseModeConfig()
        private var localhostOnly = false
        private var additionalLogWriters: List<MockzillaLogWriter> = mutableListOf()
        private var isNetworkDiscoveryEnabled: Boolean = true

        /**
         * Configures the level of Mockzilla's logging.
         *
         * @param level Defaults to `LogLevel.Info`
         */
        fun setLogLevel(level: LogLevel) = apply {
            this.logLevel = level
        }

        /**
         * Sets the port which the server will bind to. Setting port to `0` will cause the server to
         * choose its port automatically.
         *
         * @param port Port number to bind to. Use `0` for automatic port assignment.
         */
        fun setPort(port: Int): Builder = apply {
            this.port = port
        }

        /**
         * No-Op
         *
         * @param percentage Not supported
         */
        @Deprecated("Configuring failure on top level config is now not supported")
        fun setFailureProbabilityPercentage(percentage: Int) = apply {
            // No op
        }

        /**
         * Used to simulate latency: The artificial mean delay Mockzilla with add to a network request.
         *
         * Value set on individual endpoints takes priority over this value
         *
         * @param delay delay in milliseconds
         */
        @Deprecated("Delay is now constant with no variance", replaceWith = ReplaceWith("setDelayMillis"))
        fun setMeanDelayMillis(delay: Int) = apply {
            this.delay = delay
        }

        /**
         * Used to simulate latency: The artificial delay Mockzilla with add to a network request.
         * Value set on individual endpoints takes priority over this value
         *
         * @param delay delay in milliseconds
         */
        fun setDelayMillis(delay: Int) = apply {
            this.delay = delay
        }

        /**
         * Used to simulate latency:  The artificial variance in the delay Mockzillaadds to a network
         * request. Used alongside [setMeanDelayMillis] to calculate the actual artificial delay on each
         * invocation. Set this value to 0 to remove any randomness from the delay.
         *
         * Value set on individual endpoints takes priority over this value
         *
         * @param delay delay in milliseconds
         */
        @Deprecated("No longer supported, now does nothing")
        fun setDelayVarianceMillis(variance: Int) = apply {
            // No-Op
        }

        /**
         * Enable or disable release mode. See [setReleaseModeConfig] for more details
         *
         * @param isRelease `true` to enable release mode, `false` to disable.
         */
        fun setIsReleaseModeEnabled(isRelease: Boolean) = apply {
            this.isRelease = isRelease
        }

        /**
         * Setting this value to `true` means the mockzilla server will only accept calls from localhost.
         * Calls from other IPs will be blocked (including blocking the Mockzilla desktop interface)
         *
         * @param localhostOnly `true` to restrict connections to localhost only.
         */
        fun setLocalhostOnly(localhostOnly: Boolean) = apply {
            this.localhostOnly = localhostOnly
        }

        /**
         * Sets the mockzilla release config.
         *
         * By default the release mode:
         * -  Introduces rate limiting to the server
         * -  Enforces rudamentary token authentication on each request (see documentation).
         * - Only allows connections from 127.0.0.1 (i.e from apps running on the device).
         */
        fun setReleaseModeConfig(releaseConfig: ReleaseModeConfig) = apply {
            this.releaseConfig = releaseConfig
        }

        /**
         * Register an new endpoint configuration
         *
         * @param endpoint The endpoint builder to register.
         */
        fun addEndpoint(endpoint: EndpointConfiguration.Builder) = addEndpoint(endpoint.build())

        /**
         * Register an new endpoint configuration
         *
         * @param endpoint The endpoint configuration to register.
         * @return This builder, for chaining.
         */
        fun addEndpoint(endpoint: EndpointConfiguration) = apply {
            endpoints.add(endpoint)
        }

        /**
         * Register an additional log writer.
         *
         * Mockzilla logs will then log to standard output and to any additional log writers
         *
         * @param logWriter The log writer to register.
         * @return This builder, for chaining.
         */
        fun addLogWriter(logWriter: MockzillaLogWriter) = apply {
            additionalLogWriters += logWriter
        }

        /**
         * Setting this to false will stop Mockzilla from using Bonjour to broadcast itself on the network
         * Note: Broadcast is disabled in release mode regardless of this flag's value
         */
        fun setIsNetworkDiscoveryEnabled(isEnabled: Boolean) = apply {
            this.isNetworkDiscoveryEnabled = isEnabled
        }

        /**
         * Completes the builder pattern, returning an immutable config.
         *
         * @return The fully constructed [MockzillaConfig].
         */
        fun build() = MockzillaConfig(port, endpoints.map {
            it.copy(
                delay = it.delay ?: delay,
            )
        }, isRelease, localhostOnly, logLevel, releaseConfig, isNetworkDiscoveryEnabled, additionalLogWriters)

        companion object {
            const val defaultPort = 8080
        }
    }
}

/**
 * Runtime details of a started Mockzilla server, returned by `startMockzilla`. Use [mockBaseUrl]
 * as the base URL in the app under test's HTTP client to route requests through the mock server.
 *
 * @property config The configuration the server was started with.
 * @property ip The IP address the server is listening on.
 * @property mockBaseUrl Base URL for mock endpoint requests. Configure the app under test's HTTP
 * client to use this URL.
 * @property apiBaseUrl Base URL for the Mockzilla control API.
 * @property port The port the server is bound to.
 * @property authHeaderProvider Provides authentication headers for making requests to this server
 * instance.
 * @property mockzillaVersion The version of the Mockzilla library.
 */
data class MockzillaRuntimeParams(
    val config: MockzillaConfig,
    val ip: String,
    val mockBaseUrl: String,
    val apiBaseUrl: String,
    val port: Int,
    val authHeaderProvider: AuthHeaderProvider,
    val mockzillaVersion: String
)
