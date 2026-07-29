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
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.UUID
import android.util.Log
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
        private const val TAG = "NativeMockzilla"

        /**
         * Maximum time to wait for the JS side to respond to a matcher/handler
         * request before giving up. Prevents the Ktor request thread from
         * suspending forever if the JS response never arrives (e.g. the JS
         * callback throws, no listener is registered, or the bridge is torn down
         * mid-request).
         */
        private const val JS_RESPONSE_TIMEOUT_MS = 10_000L
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val pendingMatchers = ConcurrentHashMap<String, CompletableDeferred<Boolean>>()
    private val pendingHandlers = ConcurrentHashMap<String, CompletableDeferred<ReadableMap>>()

    override fun getName() = NAME

    override fun startMockzilla(config: ReadableMap, promise: Promise) {
        scope.launch {
            try {
                Log.d(TAG, "startMockzilla: building config and starting server")
                val params = startMockzilla(config.toMockzillaConfig(), reactApplicationContext)
                Log.i(
                    TAG,
                    "startMockzilla: server started (mockBaseUrl=${params.mockBaseUrl}, port=${params.port})"
                )
                promise.resolve(WritableNativeMap().apply {
                    putString("mockBaseUrl", params.mockBaseUrl)
                    putString("apiBaseUrl", params.apiBaseUrl)
                    putInt("port", params.port)
                })
            } catch (e: Exception) {
                Log.e(TAG, "startMockzilla: failed to start server", e)
                promise.reject("MOCKZILLA_START_ERROR", e.message, e)
            }
        }
    }

    override fun stopMockzilla(promise: Promise) {
        scope.launch {
            runCatching { stopMockzilla() }
                .onSuccess { Log.i(TAG, "stopMockzilla: server stopped") }
                .onFailure { Log.e(TAG, "stopMockzilla: error while stopping server", it) }
            // Fail any in-flight requests so the Ktor threads don't hang after teardown.
            failPending()
            promise.resolve(null)
        }
    }

    override fun respondToMatcher(requestId: String, matches: Boolean) {
        val deferred = pendingMatchers.remove(requestId)
        if (deferred == null) {
            Log.w(
                TAG,
                "respondToMatcher: no pending matcher for requestId=$requestId " +
                    "(already completed, timed out, or response was mis-routed)"
            )
            return
        }
        Log.d(TAG, "respondToMatcher: requestId=$requestId matches=$matches")
        deferred.complete(matches)
    }

    override fun respondToHandler(requestId: String, response: ReadableMap) {
        val deferred = pendingHandlers.remove(requestId)
        if (deferred == null) {
            Log.w(
                TAG,
                "respondToHandler: no pending handler for requestId=$requestId " +
                    "(already completed, timed out, or response was mis-routed)"
            )
            return
        }
        Log.d(TAG, "respondToHandler: requestId=$requestId")
        deferred.complete(response)
    }

    /**
     * Completes any outstanding matcher/handler deferreds with safe fallbacks so
     * suspended Ktor request threads resume instead of hanging (used on teardown).
     */
    private fun failPending() {
        val matcherCount = pendingMatchers.size
        val handlerCount = pendingHandlers.size
        if (matcherCount > 0 || handlerCount > 0) {
            Log.w(
                TAG,
                "failPending: releasing $matcherCount pending matcher(s) and " +
                    "$handlerCount pending handler(s)"
            )
        }
        pendingMatchers.keys.toList().forEach { id ->
            pendingMatchers.remove(id)?.complete(false)
        }
        pendingHandlers.keys.toList().forEach { id ->
            pendingHandlers.remove(id)?.cancel()
        }
    }

    private suspend fun callJsMatcher(key: String, req: RequestBridge): Boolean {
        val id = UUID.randomUUID().toString()
        val deferred = CompletableDeferred<Boolean>().also { pendingMatchers[id] = it }
        return try {
            Log.d(TAG, "callJsMatcher: emitting matcher request (key=$key, id=$id)")
            emitRequest(id, key, RequestEventType.EndpointMatcher, req)
            val result = withTimeoutOrNull(JS_RESPONSE_TIMEOUT_MS) { deferred.await() }
            if (result == null) {
                Log.e(
                    TAG,
                    "callJsMatcher: timed out after ${JS_RESPONSE_TIMEOUT_MS}ms waiting for JS " +
                        "response (key=$key, id=$id). Treating as no-match. This usually means the " +
                        "JS matcher threw or never called respondToMatcher."
                )
                false
            } else {
                Log.d(TAG, "callJsMatcher: received response (key=$key, id=$id, matches=$result)")
                result
            }
        } catch (e: Exception) {
            Log.e(TAG, "callJsMatcher: error awaiting JS response (key=$key, id=$id)", e)
            false
        } finally {
            pendingMatchers.remove(id)
        }
    }

    private suspend fun callJsHandler(
        type: RequestEventType,
        key: String,
        req: RequestBridge
    ): MockzillaHttpResponse {
        val id = UUID.randomUUID().toString()
        val deferred = CompletableDeferred<ReadableMap>().also { pendingHandlers[id] = it }
        return try {
            Log.d(TAG, "callJsHandler: emitting ${type.value} request (key=$key, id=$id)")
            emitRequest(id, key, type, req)
            val response = withTimeoutOrNull(JS_RESPONSE_TIMEOUT_MS) { deferred.await() }
            if (response == null) {
                Log.e(
                    TAG,
                    "callJsHandler: timed out after ${JS_RESPONSE_TIMEOUT_MS}ms waiting for JS " +
                        "response (type=${type.value}, key=$key, id=$id). Returning 500."
                )
                timeoutResponse(type, key)
            } else {
                Log.d(TAG, "callJsHandler: received response (type=${type.value}, key=$key, id=$id)")
                runCatching { response.toMockzillaHttpResponse() }.getOrElse { e ->
                    Log.e(
                        TAG,
                        "callJsHandler: failed to convert JS response (type=${type.value}, " +
                            "key=$key, id=$id)",
                        e
                    )
                    timeoutResponse(type, key)
                }
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "callJsHandler: error awaiting JS response (type=${type.value}, key=$key, id=$id)",
                e
            )
            timeoutResponse(type, key)
        } finally {
            pendingHandlers.remove(id)
        }
    }

    private fun timeoutResponse(type: RequestEventType, key: String) = MockzillaHttpResponse(
        statusCode = HttpStatusCode.InternalServerError,
        body = "Mockzilla: no JS response for ${type.value} on endpoint '$key'"
    )

    private fun emitRequest(id: String, key: String, type: RequestEventType, req: RequestBridge) {
        try {
            // IMPORTANT: this goes through the TurboModule's generated event emitter
            // (backed by the module's own EventEmitterCallback), NOT the legacy
            // global RCTDeviceEventEmitter/NativeEventEmitter bridge. The legacy
            // path relies on a bridge/interop compatibility shim that isn't
            // guaranteed to exist on every RN build, and silently drops events
            // with no exception on either side when it isn't available - which
            // is what caused matchers to hang indefinitely in production apps.
            Log.d(TAG, "emitRequest: emitting ${type.value} request (key=$key, id=$id)")
            emitOnMockzillaRequest(WritableNativeMap().apply {
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
        } catch (e: Exception) {
            // If the emit itself fails, the JS side will never respond, so complete
            // the pending deferred defensively to avoid hanging the request thread.
            Log.e(
                TAG,
                "emitRequest: failed to emit MockzillaRequest event " +
                    "(type=${type.value}, key=$key, id=$id). Releasing pending request.",
                e
            )
            pendingMatchers.remove(id)?.complete(false)
            pendingHandlers.remove(id)?.cancel()
        }
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

        Log.d(TAG, "toMockzillaConfig: converting ${eps.size()} endpoint config(s)")
        for (i in 0 until eps.size()) {
            val ep = eps.getMap(i)
            
            if (ep == null) {
                Log.w(TAG, "toMockzillaConfig: skipping null endpoint at index $i")
                continue
            }
            val key = ep.getString("key")
            
            if (key == null) {
                Log.w(TAG, "toMockzillaConfig: skipping endpoint at index $i with missing key")
                continue
            }
            val epBuilder = EndpointConfiguration.Builder(key)
                .setName(ep.getString("name") ?: key)

            if (!ep.isNull("shouldFail")) epBuilder.setShouldFail(ep.getBoolean("shouldFail"))
            if (!ep.isNull("delayMs")) epBuilder.setMeanDelayMillis(ep.getInt("delayMs"))
            if (!ep.isNull("versionCode")) epBuilder.setVersionCode(ep.getInt("versionCode"))

            epBuilder
                .setPatternMatcher {
                    val body = runCatching { bodyAsString() }
                        .onFailure { Log.w(TAG, "patternMatcher: failed to read body (key=$key)", it) }
                        .getOrDefault("")
                    callJsMatcher(key, RequestBridge(uri, headers, method.value, body))
                }
                .setDefaultHandler {
                    val body = runCatching { bodyAsString() }
                        .onFailure { Log.w(TAG, "defaultHandler: failed to read body (key=$key)", it) }
                        .getOrDefault("")
                    callJsHandler(RequestEventType.DefaultHandler, key, RequestBridge(uri, headers, method.value, body))
                }
                .setErrorHandler {
                    val body = runCatching { bodyAsString() }
                        .onFailure { Log.w(TAG, "errorHandler: failed to read body (key=$key)", it) }
                        .getOrDefault("")
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
