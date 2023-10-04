package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.utils.*
import com.apadmi.mockzilla.lib.internal.utils.allowCors
import com.apadmi.mockzilla.lib.internal.utils.respondMockzilla
import com.apadmi.mockzilla.lib.internal.utils.safeResponse
import com.apadmi.mockzilla.lib.internal.utils.toMockzillaRequest
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("TOO_LONG_FUNCTION")
internal fun Application.configureEndpoints(
    superviser: CompletableJob,
    di: DependencyInjector
) {
    routing {
        HttpMethod.DefaultMethods.forEach { method ->
            route("/local-mock/{...}", method) {
                handle {
                    withContext(coroutineContext + superviser) {
                        di.logger.i { "Responding to ${method.value}: ${call.request.uri}" }
                        safeResponse(di.logger) {
                            call.respondMockzilla(
                                di.localMockController.handleRequest(call.toMockzillaRequest(method))
                            )
                        }
                    }
                }
            }
        }
        options("/api/{...}") {
            di.logger.v { "Handling OPTIONS request: ${call.request.uri}" }
            call.allowCors()
            call.respond("")
        }
        get("/api/meta") {
            di.logger.v { "Handling GET meta: ${call.request.uri}" }
            safeResponse(di.logger) {
                call.allowCors()
                call.respond(di.metaData)
            }
        }
        get("/api/mock-data") {
            di.logger.v { "Handling GET mock-data: ${call.request.uri}" }
            safeResponse(di.logger) {
                call.allowCors()
                call.respondText(
                    JsonProvider.json.encodeToString(
                        di.webPortalApiController.getAllMockDataEntries()
                    )
                )
            }
        }
        post("/api/mock-data/{id}") {
            di.logger.v { "Handling POST mock-data: ${call.request.uri}" }
            safeResponse(di.logger) {
                val id = call.parameters["id"] ?: ""
                di.webPortalApiController.updateEntry(id, call.receive())
                call.allowCors()
                call.respond(HttpStatusCode.NoContent)
            }
        }
        delete("/api/mock-data") {
            di.logger.v { "Handling DELETE mock-data: ${call.request.uri}" }
            safeResponse(di.logger) {
                di.webPortalApiController.clearAllCaches()
                call.allowCors()
                call.respond(HttpStatusCode.NoContent)
            }
        }
        get("/api/monitor-logs") {
            safeResponse(di.logger) {
                call.allowCors()
                call.respondText(JsonProvider.json.encodeToString(MonitorLogsResponse(
                    di.metaData.appPackage, di.webPortalApiController.consumeLogEntries()
                )))
            }
        }
        get("/api/global") {
            di.logger.v { "Handling GET global: ${call.request.uri}" }
            safeResponse(di.logger) {
                val globalOverrides = di.webPortalApiController.getGlobalOverrides()
                call.allowCors()

                globalOverrides?.let {
                    call.respondText(
                        JsonProvider.json.encodeToString(globalOverrides)
                    )
                } ?: call.respond(HttpStatusCode.NoContent)
            }
        }
        post("/api/global") {
            di.logger.v { "Handling POST global: ${call.request.uri}" }
            safeResponse(di.logger) {
                di.webPortalApiController.updateGlobalOverrides(call.receive())
                call.allowCors()
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
