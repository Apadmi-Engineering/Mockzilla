package com.apadmi.mockzilla.reactnative

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

private enum class RequestEventType(val value: String) {
    EndpointMatcher("endpointMatcher"),
    DefaultHandler("defaultHandler"),
    ErrorHandler("errorHandler"),
}

class MockzillaModule(reactContext: ReactApplicationContext) :
    NativeMockzillaModuleSpec(reactContext) {

    companion object {
        const val NAME = NativeMockzillaModuleSpec.NAME
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val pendingMatchers = ConcurrentHashMap<String, CompletableDeferred<Boolean>>()
    private val pendingHandlers = ConcurrentHashMap<String, CompletableDeferred<ReadableMap>>()

    override fun getName() = NAME

    override fun startMockzilla(config: ReadableMap, promise: Promise) {
        scope.launch {
            try {
                val params = startMockzilla(config.toMockzillaConfig(), reactApplicationContext)
                promise.resolve(WritableNativeMap().apply {
                    putString("mockBaseUrl", params.mockBaseUrl)
                    putString("apiBaseUrl", params.apiBaseUrl)
                    putInt("port", params.port)
                })
            } catch (e: Exception) {
                promise.reject("MOCKZILLA_START_ERROR", e.message, e)
            }
        }
    }

    override fun stopMockzilla(promise: Promise) {
        scope.launch {
            runCatching { stopMockzilla() }
            promise.resolve(null)
        }
    }

    override fun respondToMatcher(requestId: String, matches: Boolean) {
        pendingMatchers.remove(requestId)?.complete(matches)
    }

    override fun respondToHandler(requestId: String, response: ReadableMap) {
        pendingHandlers.remove(requestId)?.complete(response)
    }

    override fun addListener(eventName: String) {}
    override fun removeListeners(count: Double) {}

    private suspend fun callJsMatcher(key: String, req: RequestBridge): Boolean {
        val id = UUID.randomUUID().toString()
        val d = CompletableDeferred<Boolean>().also { pendingMatchers[id] = it }
        emitRequest(id, key, RequestEventType.EndpointMatcher, req)
        return d.await()
    }

    private suspend fun callJsHandler(
        type: RequestEventType,
        key: String,
        req: RequestBridge
    ): MockzillaHttpResponse {
        val id = UUID.randomUUID().toString()
        val d = CompletableDeferred<ReadableMap>().also { pendingHandlers[id] = it }
        emitRequest(id, key, type, req)
        return d.await().toMockzillaHttpResponse()
    }

    private fun emitRequest(id: String, key: String, type: RequestEventType, req: RequestBridge) {
        reactApplicationContext.getJSModule(RCTDeviceEventEmitter::class.java)
            .emit("MockzillaRequest", WritableNativeMap().apply {
                putString("requestId", id)
                putString("key", key)
                putString("type", type.value)
                putMap("request", WritableNativeMap().apply {
                    putString("uri", req.uri)
                    putString("method", req.method)
                    putString("body", req.body)
                    putMap("headers", req.headers.toWritableMap())
                })
            })
    }

    private fun ReadableMap.toMockzillaConfig(): MockzillaConfig {
        val eps = getArray("endpoints") ?: Arguments.createArray()
        val builder = MockzillaConfig.Builder()

        if (!isNull("port")) builder.setPort(getInt("port"))
        if (!isNull("localHostOnly")) builder.setLocalhostOnly(getBoolean("localHostOnly"))
        if (!isNull("isNetworkDiscoveryEnabled")) {
            builder.setIsNetworkDiscoveryEnabled(getBoolean("isNetworkDiscoveryEnabled"))
        }
        if (!isNull("logLevel")) builder.setLogLevel(getString("logLevel").toMockzillaLogLevel())

        for (i in 0 until eps.size()) {
            val ep = eps.getMap(i) ?: continue
            val key = ep.getString("key") ?: continue
            val epBuilder = EndpointConfiguration.Builder(key)
                .setName(ep.getString("name") ?: key)

            if (!ep.isNull("shouldFail")) epBuilder.setShouldFail(ep.getBoolean("shouldFail"))
            if (!ep.isNull("delayMs")) epBuilder.setMeanDelayMillis(ep.getInt("delayMs"))
            if (!ep.isNull("versionCode")) epBuilder.setVersionCode(ep.getInt("versionCode"))

            epBuilder
                .setPatternMatcher {
                    val body = runCatching { bodyAsString() }.getOrDefault("")
                    callJsMatcher(key, RequestBridge(uri, headers, method.value, body))
                }
                .setDefaultHandler {
                    val body = runCatching { bodyAsString() }.getOrDefault("")
                    callJsHandler(RequestEventType.DefaultHandler, key, RequestBridge(uri, headers, method.value, body))
                }
                .setErrorHandler {
                    val body = runCatching { bodyAsString() }.getOrDefault("")
                    callJsHandler(RequestEventType.ErrorHandler, key, RequestBridge(uri, headers, method.value, body))
                }

            val presets = ep.getArray("presets") ?: Arguments.createArray()
            if (presets.size() > 0) {
                epBuilder.configureDashboardOverrides {
                    for (j in 0 until presets.size()) {
                        val p = presets.getMap(j) ?: continue
                        addPreset(
                            MockzillaHttpResponse(
                                statusCode = HttpStatusCode.fromValue(p.getInt("statusCode")),
                                headers = p.getMap("headers")?.toStringMap() ?: emptyMap(),
                                body = p.getString("body") ?: ""
                            ),
                            name = p.getString("name") ?: "Preset",
                            description = p.getString("description")
                        )
                    }
                    this
                }
            }
            builder.addEndpoint(epBuilder)
        }
        return builder.build()
    }
}

data class RequestBridge(
    val uri: String,
    val headers: Map<String, String>,
    val method: String,
    val body: String
)
