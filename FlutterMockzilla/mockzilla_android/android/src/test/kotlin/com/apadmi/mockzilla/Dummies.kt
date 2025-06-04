package com.apadmi.mockzilla

import MockzillaFlutterApi
import android.os.Handler
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import org.mockito.Mockito.mock

// Mockito can't mock this class directly, so this is a "thin" implementation that mocks the
// loggers dependencies.
class DummyProxyMockzillaLogger :
    ProxyMockzillaLogger(mock(MockzillaFlutterApi::class.java), mock(Handler::class.java)) {
    override fun log(
        logLevel: MockzillaConfig.LogLevel,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        // Intentionally blank.
    }
}