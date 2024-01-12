@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.plugin

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
internal val SimpleAuthPlugin = createApplicationPlugin(name = "SimpleAuthPlugin", ::SimpleAuthConfiguration) {
    onCall { call ->
        val header = call.request.headers[pluginConfig.headerKey]
        if (header == null || !pluginConfig.headerValueIsValid(header)) {
            call.respond(
                HttpStatusCode.Forbidden,
                "Forbidden - Mockzilla is in release mode and your token is missing or invalid"
            )
        }
    }
}

@Suppress("USE_DATA_CLASS")
internal class SimpleAuthConfiguration {
    var headerKey: String = ""
    var headerValueIsValid: suspend (String) -> Boolean = { true }
}
