package com.apadmi.mockzilla

import BridgeLogLevel
import MockzillaFlutterApi
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.service.MockzillaLogWriter

class ProxyMockzillaLogger(
    private val flutterApi: MockzillaFlutterApi
) : MockzillaLogWriter {

    override fun log(
        logLevel: MockzillaConfig.LogLevel,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
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