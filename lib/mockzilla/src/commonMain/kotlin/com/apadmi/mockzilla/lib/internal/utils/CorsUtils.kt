package com.apadmi.mockzilla.lib.internal.utils

import io.ktor.server.application.*

internal fun ApplicationCall.allowCors() = with(response.headers) {
    append("Access-Control-Allow-Origin", "*")
    append("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE")
    append("Access-Control-Allow-Headers", "Content-Type")
}
