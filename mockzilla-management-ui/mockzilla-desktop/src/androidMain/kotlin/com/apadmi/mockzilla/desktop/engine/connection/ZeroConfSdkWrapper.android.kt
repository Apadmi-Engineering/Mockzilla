package com.apadmi.mockzilla.desktop.engine.connection

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope

internal actual class ZeroConfSdkWrapper actual constructor(serviceType: String, scope: CoroutineScope) {
    actual fun setListener(listener: suspend (DeviceDiscoveryEvent) -> Unit) {
        Logger.i { "Skipping ZeroConf setup: Not available on Android target" }
    }

    actual suspend fun stop() = Unit
}
