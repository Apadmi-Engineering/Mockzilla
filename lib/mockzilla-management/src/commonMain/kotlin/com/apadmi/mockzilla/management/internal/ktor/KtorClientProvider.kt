package com.apadmi.mockzilla.management.internal.ktor

import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json

internal object KtorClientProvider {
    fun createKtorClient(engine: HttpClientEngine? = null) =
            engine?.let {
                HttpClient(engine) {
                    httpClientConfig()
                }
            } ?: HttpClient { httpClientConfig() }

    private fun HttpClientConfig<*>.httpClientConfig() {
        install(ContentNegotiation) {
             json(JsonProvider.json)
        }

        install(Logging) {
            this.logger = logger
            this.level = LogLevel.INFO
        }

        install(Resources)
    }
}
