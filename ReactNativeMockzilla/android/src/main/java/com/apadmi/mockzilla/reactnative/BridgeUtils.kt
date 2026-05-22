package com.apadmi.mockzilla.reactnative

import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableNativeMap
import io.ktor.http.HttpStatusCode

fun ReadableMap.toMockzillaHttpResponse() = MockzillaHttpResponse(
    statusCode = HttpStatusCode.fromValue(getInt("statusCode")),
    headers = getMap("headers")?.toStringMap() ?: emptyMap(),
    body = getString("body") ?: "",
)

fun ReadableMap.toStringMap(): Map<String, String> = buildMap {
    val iter = keySetIterator()
    while (iter.hasNextKey()) {
        val k = iter.nextKey()
        put(k, getString(k) ?: "")
    }
}

fun Map<String, String>.toWritableMap() = WritableNativeMap().also { m ->
    forEach { (k, v) -> m.putString(k, v) }
}

fun String?.toMockzillaLogLevel() = when (this?.uppercase()) {
    "DEBUG" -> MockzillaConfig.LogLevel.Debug
    "ERROR" -> MockzillaConfig.LogLevel.Error
    "VERBOSE" -> MockzillaConfig.LogLevel.Verbose
    "WARN" -> MockzillaConfig.LogLevel.Warn
    "ASSERT" -> MockzillaConfig.LogLevel.Assert
    else -> MockzillaConfig.LogLevel.Info
}
