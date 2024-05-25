package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.plugin.SimpleAuthPlugin
import com.apadmi.mockzilla.lib.internal.service.AuthenticationConstants
import com.apadmi.mockzilla.lib.internal.service.TokensService
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.internal.utils.environment
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.routing.*

import kotlinx.coroutines.*

private var server: ApplicationEngine? = null
private var job: Job? = null

private fun Application.setupReleaseMode(config: MockzillaConfig, tokensService: TokensService) {
    install(SimpleAuthPlugin) {
        headerKey = AuthenticationConstants.headerKey
        headerValueIsValid = tokensService::isTokenValid
    }

    install(RateLimit) {
        global {
            rateLimiter(
                limit = config.releaseModeConfig.rateLimit,
                refillPeriod = config.releaseModeConfig.rateLimitRefillPeriod
            )
        }
    }
}

internal fun startServer(port: Int, di: DependencyInjector) = runBlocking {
    stopServer()

    val job = SupervisorJob().also { job = it }
    val serverEngine = embeddedServer(CIO, configure = {
        connectionIdleTimeoutSeconds = 1
        reuseAddress = true
    }, environment = environment(
        port,
        // Only allow localhost connections in release mode. This stops anyone on the network from
        // being able to hit the server.
        host = if (di.config.isRelease || di.config.localhostOnly) "127.0.0.1" else "0.0.0.0"
    ) {
        install(IgnoreTrailingSlash)
        install(ContentNegotiation) {
            json(JsonProvider.json)
        }

        if (di.config.isRelease) {
            setupReleaseMode(di.config, di.tokensService)
        }

        configureEndpoints(job, di)
    }).apply {
        server = this
        start(wait = false)
    }

    val actualPort = serverEngine.resolvedConnectors().firstOrNull()?.port
        ?: throw Exception("Could not determine runtime port")

    startNetworkDiscoveryBroadcastIfNeeded(job, di, actualPort)

    MockzillaRuntimeParams(
        di.config,
        "http://127.0.0.1:$actualPort/local-mock",
        "http://127.0.0.1:$actualPort/api",
        actualPort,
        di.authHeaderProvider,
        BuildKonfig.VERSION_NAME,
    )
}

internal fun stopServer() = runBlocking {
    job?.cancel()
    server?.stop()
}

private fun startNetworkDiscoveryBroadcastIfNeeded(
    job: CompletableJob,
    di: DependencyInjector,
    port: Int
) = CoroutineScope(job).launch {
    if (!di.config.isRelease && di.config.isNetworkDiscoveryEnabled) {
        di.zeroConfDiscoveryService.makeDiscoverable(di.metaData, port)
    } else {
        di.logger.i { "Skipping network discovery" }
    }
}
