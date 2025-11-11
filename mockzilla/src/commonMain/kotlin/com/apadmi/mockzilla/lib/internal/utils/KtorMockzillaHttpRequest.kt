package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class KtorMockzillaHttpRequest internal constructor(
    private val call: ApplicationCall,
    override val method: HttpMethod
) : MockzillaHttpRequest {
    override val uri: String get() = call.request.uri
    override val headers: Map<String, String> by lazy {
        call.request.headers.entries()
            .associate { it.key to it.value.joinToString() }
    }
    private var bodyBytesCache: ByteArray? = null

    /**
     * The underlying ktor [ApplicationRequest](https://api.ktor.io/ktor-server/ktor-server-core/io.ktor.server.request/-application-request/index.html).
     */
    val underlyingKtorRequest: ApplicationRequest get() = call.request

    override suspend fun bodyAsBytes() = bodyBytesCache ?: run {
        return call.readRequestBodyBytes().also {
            bodyBytesCache = it
        }
    }
    override suspend fun bodyAsString(): String = bodyAsBytes().decodeToString()
}

internal fun ApplicationCall.toMockzillaRequest(
    method: HttpMethod,
) = KtorMockzillaHttpRequest(
    this,
    method
)

internal suspend fun ApplicationCall.respondMockzilla(mockzillaHttpResponse: MockzillaHttpResponse) {
    mockzillaHttpResponse.headers.forEach { entry ->
        response.headers.append(entry.key, entry.value)
    }

    respondText(
        text = mockzillaHttpResponse.body,
        status = mockzillaHttpResponse.statusCode
    )
}

private suspend fun ApplicationCall.readRequestBodyBytes() = if ((request.contentLength() ?: 0) > 0) {
    receive()
} else {
    ByteArray(0)
}
