package com.apadmi.mockzilla.lib.models

import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import com.apadmi.mockzilla.lib.service.MockzillaLogWriter

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * @property port
 * @property endpoints
 * @property logLevel
 * @property isRelease
 * @property releaseModeConfig
 * @property localhostOnly
 * @property additionalLogWriters
 */
data class MockzillaConfig(
    val port: Int,
    val endpoints: List<EndpointConfiguration>,
    val isRelease: Boolean,
    val localhostOnly: Boolean,
    val logLevel: LogLevel,
    val releaseModeConfig: ReleaseModeConfig,
    val additionalLogWriters: List<MockzillaLogWriter>
) {
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
         * choose it's port auto-magically.
         *
         * @param port
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
         * @param isRelease
         */
        fun setIsReleaseModeEnabled(isRelease: Boolean) = apply {
            this.isRelease = isRelease
        }

        /**
         * Setting this value to `true` means the mockzilla server will only accept calls from localhost.
         * Calls from other IPs will be blocked (including blocking the Mockzilla web interface)
         *
         * @param localhostOnly
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
         * @param endpoint
         */
        fun addEndpoint(endpoint: EndpointConfiguration.Builder) = addEndpoint(endpoint.build())

        /**
         * Register an new endpoint configuration
         *
         * @param endpoint
         * @return
         */
        fun addEndpoint(endpoint: EndpointConfiguration) = apply {
            endpoints.add(endpoint)
        }

        /**
         * Register an additional log writer.
         *
         * Mockzilla logs will then log to standard output and to any additional log writers
         *
         * @param logWriter
         * @return
         */
        fun addLogWriter(logWriter: MockzillaLogWriter) = apply {
            additionalLogWriters += logWriter
        }

        /**
         * Completes the builder pattern, returning an immutable config.
         *
         * @return
         */
        fun build() = MockzillaConfig(port, endpoints.map {
            it.copy(
                delay = it.delay ?: delay,
            )
        }, isRelease, localhostOnly, logLevel, releaseConfig, additionalLogWriters)

        companion object {
            private const val defaultPort = 8080
        }
    }
}

/**
 * @property config
 * @property mockBaseUrl
 * @property apiBaseUrl
 * @property port
 * @property authHeaderProvider
 * @property mockzillaVersion
 */
data class MockzillaRuntimeParams(
    val config: MockzillaConfig,
    val mockBaseUrl: String,
    val apiBaseUrl: String,
    val port: Int,
    val authHeaderProvider: AuthHeaderProvider,
    val mockzillaVersion: String
)
