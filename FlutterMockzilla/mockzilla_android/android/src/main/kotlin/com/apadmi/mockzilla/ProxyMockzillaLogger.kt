package com.apadmi.mockzilla

import BridgeLogLevel
import MockzillaFlutterApi
import android.os.Handler
import android.os.Looper
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.service.MockzillaLogWriter

class ProxyMockzillaLogger(
    private val flutterApi: MockzillaFlutterApi
) : MockzillaLogWriter {
    val uiThreadHandler = Handler(Looper.getMainLooper())

    override fun log(
        logLevel: MockzillaConfig.LogLevel,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        uiThreadHandler.post {
            flutterApi.log(
                BridgeLogLevel.fromNative(logLevel),
                message,
                tag,
                throwable?.localizedMessage
            ) {
                // Intentionally blank as this call is fire and forget.
            }
        }
    }
}