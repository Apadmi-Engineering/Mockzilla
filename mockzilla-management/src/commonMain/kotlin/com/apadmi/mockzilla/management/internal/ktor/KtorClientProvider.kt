package com.apadmi.mockzilla.management.internal.ktor

import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
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
    fun createKtorClient(engine: HttpClientEngine? = null, logger: Logger = Logger.SIMPLE) =
        engine?.let {
            HttpClient(engine) {
                httpClientConfig(logger)
            }
        } ?: HttpClient { httpClientConfig(logger) }

    private fun HttpClientConfig<*>.httpClientConfig(logger: Logger) {
        install(ContentNegotiation) {
            json(JsonProvider.json)
        }

        install(Logging) {
            this.logger = logger
            this.level = LogLevel.ALL
            filter { request ->
                request.headers[CustomHeaders.HideFromLogs]?.toBoolean() != true
            }
        }

        install(Resources)
    }
}
