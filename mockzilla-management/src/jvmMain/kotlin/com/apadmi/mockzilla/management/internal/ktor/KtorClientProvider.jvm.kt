package com.apadmi.mockzilla.management.internal.ktor

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.net.Proxy

internal actual fun createPlatformKtorClient(
    disableProxy: Boolean,
    configure: HttpClientConfig<*>.() -> Unit
) = HttpClient(OkHttp) {
    engine {
        if (disableProxy) {
            preconfigured = OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)
                .protocols(listOf(Protocol.HTTP_1_1))
                .build()
        }
    }

    configure()
}
