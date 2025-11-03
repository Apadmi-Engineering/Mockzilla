package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.msw.MswBrowser
import com.apadmi.mockzilla.lib.internal.msw.ServiceWorkerInstance
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.yield

private var globalWorker: ServiceWorkerInstance? = null

@OptIn(DelicateCoroutinesApi::class)
internal actual suspend fun startServer(
    port: Int,  // Port isn't really real on JS, it's just kept consistent with other targets
    di: DependencyInjector
): MockzillaRuntimeParams {
    // Setup the worker with that handler
    val actualPort = port.takeUnless { it == 0 } ?: MockzillaConfig.Builder.defaultPort
    val baseUrl = "http://localhost:$actualPort"

    val worker = globalWorker ?: MswBrowser.setupWorker()
    worker.use(*configureEndpoints(di, baseUrl, GlobalScope))
    globalWorker = worker

    if (!worker.context.isMockingEnabled) {
        worker.start().await()
    }

    return MockzillaRuntimeParams(
        config = di.config,
        ip = "localhost",
        mockBaseUrl = "$baseUrl/local-mock",
        apiBaseUrl = "$baseUrl/api",
        port = actualPort,
        authHeaderProvider = object : AuthHeaderProvider {
            override suspend fun generateHeader() =
                AuthHeaderProvider.Header("Unsupported", "Unsupported on Mockzilla JS")
        },
        mockzillaVersion = BuildKonfig.VERSION_NAME
    )
}

internal actual suspend fun stopServer() {
    globalWorker?.resetHandlers()?.await()
    yield()

    if (globalWorker?.context?.isMockingEnabled == true) {
        globalWorker?.stop()
    }
    yield()
}
