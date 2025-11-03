package com.apadmi.mockzilla.lib.internal.utils

import io.ktor.server.application.*

internal object CorsUtils {
    val allowAllHeaders = mapOf(
        "Access-Control-Allow-Origin" to "*",
        "Access-Control-Allow-Methods" to "GET, POST, OPTIONS, PUT, DELETE",
        "Access-Control-Allow-Headers" to "Content-Type",
    )
}
internal fun ApplicationCall.allowCors() = with(response.headers) {
    CorsUtils.allowAllHeaders.forEach { (key, value) ->
        append(key, value)
    }
}
