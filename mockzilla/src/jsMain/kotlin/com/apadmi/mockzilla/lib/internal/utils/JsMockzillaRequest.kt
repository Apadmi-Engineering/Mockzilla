package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest

import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.utils.io.core.toByteArray
import org.w3c.fetch.Request as JsRequest

import kotlinx.coroutines.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class JsMockzillaRequest(private val jsRequest: JsRequest) : MockzillaHttpRequest {
    private val lock = Mutex()
    private var bodyCache: String? = null
    override val uri: String = Url(jsRequest.url).encodedPath
    override val headers: Map<String, String> = run {
        // The kotlin typedef of headers seems to be missing the iterator
        val dynamicHeaders: dynamic = jsRequest.headers
        val newHeaders = mutableMapOf<String, String>()
        dynamicHeaders.forEach { key, value ->
            newHeaders.put(key, value)
        }
        newHeaders
    }
    override val method: HttpMethod =
        HttpMethod.DefaultMethods.first { method -> method.value.lowercase() == jsRequest.method.lowercase() }

    val underlyingRequest: JsRequest get() = jsRequest

    override suspend fun bodyAsBytes(): ByteArray = bodyAsString().toByteArray()

    override suspend fun bodyAsString(): String = bodyCache ?: lock.withLock {
        return bodyCache ?: jsRequest.text().await().also {
            bodyCache = it
        }
    }
}
