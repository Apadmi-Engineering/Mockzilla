package com.apadmi.mockzilla.lib.internal.utils

import co.touchlab.kermit.Logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*

internal suspend fun PipelineContext<Unit, ApplicationCall>.safeResponse(
    logger: Logger,
    block: suspend (PipelineContext<Unit, ApplicationCall>) -> Unit,
) = try {
    this.call.response.headers.append("Connection", "close")
    block(this)
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

internal fun environment(
    port: Int,
    host: String,
    module: Application.() -> Unit
) = applicationEngineEnvironment {
    connector {
        this.port = port
        this.host = host
    }

    module(module)
}
