package com.apadmi.mockzilla

import ApiMockzillaConfig
import ApiMockzillaHttpRequest
import ApiMockzillaRuntimeParams
import MockzillaFlutterApi
import MockzillaHostApi
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking

class MockzillaAndroid(
    private val flutterApi: MockzillaFlutterApi,
    private val context: Context
): MockzillaHostApi {

    val uiThreadHandler = Handler(Looper.getMainLooper())

    override fun startServer(config: ApiMockzillaConfig): ApiMockzillaRuntimeParams {
        val nativeConfig = config.toNative(
            { key -> callEndpointMatcher(this, key) },
            { callDefaultHandler(this) },
            { callErrorHandler(this) }
        )
        val nativeRuntimeParams = startMockzilla(nativeConfig, context)
        return ApiMockzillaRuntimeParams.fromNative(nativeRuntimeParams)
    }

    override fun stopServer() {
        stopMockzilla()
    }

    private fun callEndpointMatcher(request: MockzillaHttpRequest, key: String): Boolean {
        val completer = CompletableDeferred<Boolean>()
        uiThreadHandler.post {
            flutterApi.endpointMatcher(ApiMockzillaHttpRequest.fromNative(request)) {
                completer.complete(it.getOrThrow() == key)
            }
        }
        return runBlocking { completer.await() }
    }

    private fun callDefaultHandler(request: MockzillaHttpRequest): MockzillaHttpResponse {
        val completer = CompletableDeferred<MockzillaHttpResponse>()
        uiThreadHandler.post {
            flutterApi.defaultHandler(ApiMockzillaHttpRequest.fromNative(request)) {
                completer.complete(it.getOrThrow().toNative())
            }
        }
        return runBlocking { completer.await() }
    }

    private fun callErrorHandler(request: MockzillaHttpRequest): MockzillaHttpResponse {
        val completer = CompletableDeferred<MockzillaHttpResponse>()
        uiThreadHandler.post {
            flutterApi.errorHandler(ApiMockzillaHttpRequest.fromNative(request)) {
                completer.complete(it.getOrThrow().toNative())
            }
        }
        return runBlocking { completer.await() }
    }
}