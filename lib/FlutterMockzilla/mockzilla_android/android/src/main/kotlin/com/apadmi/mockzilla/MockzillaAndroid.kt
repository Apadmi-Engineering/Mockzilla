package com.apadmi.mockzilla

import ApiMockzillaConfig
import ApiMockzillaHttpRequest
import ApiMockzillaRuntimeParams
import MockzillaFlutterApi
import MockzillaHostApi
import android.content.Context
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

    override fun startServer(config: ApiMockzillaConfig): ApiMockzillaRuntimeParams {
        val nativeConfig = config.toModel(
            { callEndpointMatcher(this) },
            { callDefaultHandler(this) },
            { callErrorHandler(this) }
        )
        val nativeRuntimeParams = startMockzilla(nativeConfig, context)
        return ApiMockzillaRuntimeParams.fromModel(nativeRuntimeParams)
    }

    override fun stopServer() {
        stopMockzilla()
    }

    private fun callEndpointMatcher(request: MockzillaHttpRequest): Boolean {
        val completer = CompletableDeferred<Boolean>()
        flutterApi.endpointMatcher(ApiMockzillaHttpRequest.fromModel(request)) {
            completer.complete(it.getOrThrow())
        }
        return runBlocking { completer.await() }
    }

    private fun callDefaultHandler(request: MockzillaHttpRequest): MockzillaHttpResponse {
        val completer = CompletableDeferred<MockzillaHttpResponse>()
        flutterApi.defaultHandler(ApiMockzillaHttpRequest.fromModel(request)) {
            completer.complete(it.getOrThrow().toModel())
        }
        return  runBlocking { completer.await() }
    }

    private fun callErrorHandler(request: MockzillaHttpRequest): MockzillaHttpResponse {
        val completer = CompletableDeferred<MockzillaHttpResponse>()
        flutterApi.errorHandler(ApiMockzillaHttpRequest.fromModel(request)) {
            completer.complete(it.getOrThrow().toModel())
        }
        return runBlocking { completer.await() }
    }
}