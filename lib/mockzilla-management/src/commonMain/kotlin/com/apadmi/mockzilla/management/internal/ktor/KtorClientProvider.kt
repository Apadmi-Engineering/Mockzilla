package com.apadmi.mockzilla.management.internal.ktor

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources

private val ConnectionConfig.baseUrl get() = "http://$ip:$port/api"

internal object KtorClientProvider {
    fun createKtorClient(connectionConfig: ConnectionConfig, engine: HttpClientEngine? = null) =
            engine?.let {
                HttpClient(engine) {
                    httpClientConfig(connectionConfig.baseUrl)
                }
            } ?: HttpClient { httpClientConfig(connectionConfig.baseUrl) }

    private fun HttpClientConfig<*>.httpClientConfig(baseUrl: String) {
        install(ContentNegotiation) {
            // json(JsonProvider.json)
        }

        install(Logging) {
            this.logger = logger
            this.level = LogLevel.INFO
        }

        install(Resources)
        defaultRequest {
            url(baseUrl)
        }
    }
}
