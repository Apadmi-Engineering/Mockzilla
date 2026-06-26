package com.apadmi.mockzilla.management.internal.ktor

import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json

internal object CustomHeaders {
    const val HideFromLogs = "hide-from-logs"
}

internal object KtorClientProvider {
    fun createKtorClient(
        disableProxy: Boolean,
        logger: Logger = Logger.SIMPLE
    ) = createPlatformKtorClient(disableProxy) {
        httpClientConfig(logger)
    }

    private fun HttpClientConfig<*>.httpClientConfig(logger: Logger) {
        install(ContentNegotiation) {
            json(JsonProvider.json)
        }

        install(Logging) {
            this.logger = object : Logger {
                override fun log(message: String) {
                    // Combines the multiline log into one line to stop cluttering the output
                    val tidied = message.lineSequence()
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .joinToString(" ⏐ ")
                    co.touchlab.kermit.Logger.v(tag = "HTTP Client") { tidied }
                }
            }
            this.level = LogLevel.INFO
            filter { request ->
                request.headers[CustomHeaders.HideFromLogs]?.toBoolean() != true
            }
        }

        install(Resources)
    }
}

internal expect fun createPlatformKtorClient(
    disableProxy: Boolean,
    configure: HttpClientConfig<*>.() -> Unit
): HttpClient
