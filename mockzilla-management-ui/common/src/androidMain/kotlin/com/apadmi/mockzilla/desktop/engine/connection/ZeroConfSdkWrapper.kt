package com.apadmi.mockzilla.desktop.engine.connection

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope

actual class ZeroConfSdkWrapper actual constructor(serviceType: String, scope: CoroutineScope) {
    actual fun setListener(listener: suspend (ServiceInfoWrapper) -> Unit) {
        Logger.i { "Skipping ZeroConf setup: Not available on Android target (yet!)" }
    }
}
