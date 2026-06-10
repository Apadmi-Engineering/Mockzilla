package com.apadmi.mockzilla.management.internal.ktor

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

internal actual fun createPlatformKtorClient(
    disableProxy: Boolean,
    configure: HttpClientConfig<*>.() -> Unit
) = HttpClient {
    configure()
}
