package com.apadmi.mockzilla

import android.content.Context
import android.os.Handler
import android.os.Looper

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla

import BridgeMockzillaConfig
import BridgeMockzillaHttpRequest
import BridgeMockzillaRuntimeParams
import MockzillaFlutterApi
import MockzillaHostApi
import io.ktor.http.HttpStatusCode

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
class MockzillaAndroid(
    private val flutterApi: MockzillaFlutterApi,
    private val proxyMockzillaLogger: ProxyMockzillaLogger,
    private val context: Context
) : MockzillaHostApi {
    val uiThreadHandler = Handler(Looper.getMainLooper())

    override fun startServer(config: BridgeMockzillaConfig): BridgeMockzillaRuntimeParams {
        val nativeConfig = config.toNative(
            { key -> isMatchedEndpoint(this, key) },
            { key -> callDefaultHandler(this, key) },
            { key -> callErrorHandler(this, key) },
            proxyMockzillaLogger,
        )
        val nativeRuntimeParams = startMockzilla(nativeConfig, context)
        return BridgeMockzillaRuntimeParams.fromNative(nativeRuntimeParams)
    }

    override fun stopServer() {
        stopMockzilla()
    }

    private suspend fun isMatchedEndpoint(request: MockzillaHttpRequest, key: String): Boolean {
        val completer: CompletableDeferred<Boolean> = CompletableDeferred()
        uiThreadHandler.post {
            runBlocking {
                flutterApi.endpointMatcher(BridgeMockzillaHttpRequest.fromNative(request), key) {
                    // Return default of `false` if error occurs.
                    completer.complete(it.getOrElse { false })
                }
            }
        }
        return completer.await()
    }

    private suspend fun callDefaultHandler(
        request: MockzillaHttpRequest,
        key: String
    ): MockzillaHttpResponse {
        val completer: CompletableDeferred<MockzillaHttpResponse> = CompletableDeferred()
        uiThreadHandler.post {
            runBlocking {
                flutterApi.defaultHandler(
                    BridgeMockzillaHttpRequest.fromNative(request),
                    key
                ) { bridgeResult ->
                    // Return default of 500 error if error occurs.
                    completer.complete(bridgeResult.map {
                        it.toNative()
                    }.getOrElse {
                        MockzillaHttpResponse(statusCode = HttpStatusCode.InternalServerError)
                    })
                }
            }
        }
        return completer.await()
    }

    private suspend fun callErrorHandler(
        request: MockzillaHttpRequest,
        key: String
    ): MockzillaHttpResponse {
        val completer: CompletableDeferred<MockzillaHttpResponse> = CompletableDeferred()
        uiThreadHandler.post {
            runBlocking {
                flutterApi.errorHandler(
                    BridgeMockzillaHttpRequest.fromNative(request),
                    key
                ) { bridgeResult ->
                    // Return default of 500 error if error occurs.
                    completer.complete(bridgeResult.map {
                        it.toNative()
                    }.getOrElse {
                        MockzillaHttpResponse(statusCode = HttpStatusCode.InternalServerError)
                    })
                }
            }
        }
        return completer.await()
    }
}
