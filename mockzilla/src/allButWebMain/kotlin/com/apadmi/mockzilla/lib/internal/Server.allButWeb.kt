package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.plugin.SimpleAuthPlugin
import com.apadmi.mockzilla.lib.internal.service.AuthenticationConstants
import com.apadmi.mockzilla.lib.internal.service.TokensService
import com.apadmi.mockzilla.lib.internal.utils.AddressAlreadyInUseException
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.internal.utils.isSomeMatchInChain
import com.apadmi.mockzilla.lib.internal.utils.multiPlatformIo
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.PortConflictException

import co.touchlab.kermit.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.response.respondText
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.RoutingContext
import io.ktor.utils.io.CancellationException

import kotlinx.coroutines.*

private var server: ApplicationEngine? = null
private var job: Job? = null

internal suspend fun RoutingContext.safeResponse(
    logger: Logger,
    block: suspend (RoutingCall) -> Unit,
) = try {
    this.call.response.headers.append("Connection", "close")
    block(call)
} catch (e: Exception) {
    logger.e("Unexpected exception", e)

    // Letting the server handle the 500 in the case the server is about to be killed, otherwise
    // the port isn't freed correctly.
    if (e !is CancellationException && e !is kotlin.coroutines.cancellation.CancellationException) {
        call.respondText(
            text = e.stackTraceToString(),
            status = HttpStatusCode.InternalServerError
        )
    }
    Unit
}

private fun Application.setupReleaseMode(
    config: MockzillaConfig,
    tokensService: TokensService
) {
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

private fun Application.setupServerEnvironment(job: CompletableJob, di: DependencyInjector) {
    install(IgnoreTrailingSlash)
    install(ContentNegotiation) {
        json(JsonProvider.json)
    }
    if (di.config.isRelease) {
        setupReleaseMode(di.config, di.tokensService)
    }
    configureEndpoints(job, di)
}

private fun EmbeddedServer<*, *>.startWithErrorHandling(di: DependencyInjector) = try {
    start(wait = false)
} catch (e: Exception) {
    if (e.isSomeMatchInChain { it is AddressAlreadyInUseException }) {
        PortConflictException(di.config.port, e).also { exception ->
            di.logger.e(exception.message, exception)
            throw exception
        }
    } else {
        throw e
    }
}

internal actual suspend fun startServer(port: Int, di: DependencyInjector): MockzillaRuntimeParams {
    stopServer()

    val job = SupervisorJob().also { job = it }
    val serverEngine = embeddedServer(CIO, configure = {
        connectionIdleTimeoutSeconds = 1
        reuseAddress = true

        connector {
            this.port = port
            // Only allow localhost connections in release mode. This stops anyone on the network from
            // being able to hit the server.
            this.host =
                    if (di.config.isRelease || di.config.localhostOnly) "127.0.0.1" else "0.0.0.0"
        }
    }).apply {
        application.setupServerEnvironment(job = job, di = di)
        server = this.application.engine
        startWithErrorHandling(di)
    }

    val actualPort = serverEngine.application.engine.resolvedConnectors()
        .firstOrNull()
        ?.port
        ?: throw Exception("Could not determine runtime port")

    startNetworkDiscoveryBroadcastIfNeeded(job, di, actualPort)

    return MockzillaRuntimeParams(
        config = di.config,
        ip = "127.0.0.1",
        mockBaseUrl = "http://127.0.0.1:$actualPort/local-mock",
        apiBaseUrl = "http://127.0.0.1:$actualPort/api",
        port = actualPort,
        authHeaderProvider = di.authHeaderProvider,
        mockzillaVersion = BuildKonfig.VERSION_NAME,
    )
}

internal actual suspend fun stopServer() {
    job?.cancel()
    server?.stop()
}

private fun startNetworkDiscoveryBroadcastIfNeeded(
    job: CompletableJob,
    di: DependencyInjector,
    port: Int
) = CoroutineScope(job).launch(Dispatchers.multiPlatformIo) {
    if (!di.config.isRelease && di.config.isNetworkDiscoveryEnabled) {
        di.logger.i { "Starting network discovery" }
        di.zeroConfDiscoveryService.makeDiscoverable(di.metaData, port)
    } else {
        di.logger.i { "Skipping network discovery" }
    }
}
