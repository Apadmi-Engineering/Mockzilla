package com.apadmi.mockzilla.management.internal.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual fun createPlatformKtorClient(
    disableProxy: Boolean,
    configure: io.ktor.client.HttpClientConfig<*>.() -> Unit
) = HttpClient(Darwin) {
    engine {
        if (disableProxy) {
            configureSession {
                // Empty dictionary means no proxy
                connectionProxyDictionary = emptyMap<Any?, Any?>()
            }
        }
    }

    configure()
}
