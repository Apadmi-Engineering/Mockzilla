package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.models.ClearCachesRequestDto
import com.apadmi.mockzilla.lib.internal.models.MockDataResponseDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigPatchRequestDto
import com.apadmi.mockzilla.lib.internal.utils.allowCors
import com.apadmi.mockzilla.lib.internal.utils.fetchAppIconBytes
import com.apadmi.mockzilla.lib.internal.utils.respondMockzilla
import com.apadmi.mockzilla.lib.internal.utils.toMockzillaRequest
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.withContext

@Suppress("TOO_LONG_FUNCTION")
internal fun Application.configureEndpoints(
    supervisor: CompletableJob,
    di: DependencyInjector
) {
    routing {
        HttpMethod.DefaultMethods.forEach { method ->
            route("/local-mock/{...}", method) {
                handle {
                    withContext(coroutineContext + supervisor) {
                        di.logger.i { "Responding to ${method.value}: ${call.request.uri}" }
                        safeResponse(di.logger) { call ->
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
            safeResponse(di.logger) { call ->
                call.allowCors()
                call.respond(di.metaData)
            }
        }
        get("/api/mock-data") {
            di.logger.v { "Handling GET mock-data: ${call.request.uri}" }
            safeResponse(di.logger) { call ->
                call.allowCors()
                call.respond(
                    MockDataResponseDto(
                        di.managementApiController.getAllMockDataEntries()
                    )
                )
            }
        }
        get("/api/mock-data/{key}/dashboard-config") {
            di.logger.v { "Handling GET mock-data presets: ${call.request.uri}" }
            safeResponse(di.logger) { call ->
                call.allowCors()
                call.respond(di.managementApiController.getDashboardConfig(call.extractKey()))
            }
        }
        patch("/api/mock-data") {
            di.logger.v { "Handling POST mock-data: ${call.request.uri}" }
            safeResponse(di.logger) { call ->
                call.allowCors()
                val patches = call.receive<SerializableEndpointConfigPatchRequestDto>().entries
                di.managementApiController.patchEntries(patches)
                call.respond(HttpStatusCode.Created)
            }
        }
        delete("/api/mock-data/all") {
            di.logger.v { "Handling DELETE mock-data: ${call.request.uri}" }
            safeResponse(di.logger) { call ->
                di.managementApiController.clearAllCaches()
                call.allowCors()
                call.respond(HttpStatusCode.NoContent)
            }
        }
        delete("/api/mock-data") {
            di.logger.v { "Handling DELETE mock-data: ${call.request.uri}" }
            safeResponse(di.logger) { call ->
                di.managementApiController.clearCache(call.receive<ClearCachesRequestDto>().keys)
                call.allowCors()
                call.respond(HttpStatusCode.NoContent)
            }
        }
        get("/api/monitor-logs") {
            safeResponse(di.logger) { call ->
                call.allowCors()
                call.respond(
                    MonitorLogsResponse(
                        di.metaData.appPackage, di.managementApiController.consumeLogEntries()
                    )
                )
            }
        }
        get("/api/monitor-logs/poll") {
            safeResponse(di.logger) { call ->
                call.allowCors()
                val since = call.request.queryParameters["since"]?.toLongOrNull()
                call.respond(
                    MonitorLogsResponse(di.metaData.appPackage, di.managementApiController.getLogsSince(since))
                )
            }
        }
        get("/api/monitor-logs/{logId}") {
            safeResponse(di.logger) { call ->
                call.allowCors()
                val logId = call.parameters["logId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@safeResponse
                }
                val detail = di.managementApiController.getLogDetail(logId)
                if (detail != null) call.respond(detail) else call.respond(HttpStatusCode.NotFound)
            }
        }
        delete("/api/monitor-logs") {
            safeResponse(di.logger) { call ->
                call.allowCors()
                di.managementApiController.clearAllLogEntries()
                call.respond(HttpStatusCode.NoContent)
            }
        }
        get("/api/app-icon") {
            safeResponse(di.logger) { call ->
                call.allowCors()
                val iconBytes = fetchAppIconBytes(di.platformConfig)
                iconBytes?.let {
                    call.response.header(HttpHeaders.CacheControl, "max-age=3600, immutable")
                    call.respondBytes(iconBytes, ContentType.Image.PNG)
                } ?: call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun ApplicationCall.extractKey() = parameters["key"]?.takeUnless {
    it.isBlank()
}?.let { EndpointConfiguration.Key(it) } ?: throw Exception("No key found in URL")
