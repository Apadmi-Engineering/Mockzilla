package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking

internal class DefaultMockzillaHttpRequest(
    private val call: ApplicationCall,
    override val method: HttpMethod
) : MockzillaHttpRequest {
    override val uri: String get() = call.request.uri
    override val headers: Map<String, String> by lazy {
        call.request.headers.entries()
            .associate { it.key to it.value.joinToString() }
    }
    private val bodyBytes: ByteArray by lazy {
        runBlocking { call.readRequestBodyBytes() }
    }

    @Deprecated("`body`is deprecated", replaceWith = ReplaceWith("bodyAsString()"))
    override val body: String
        get() = bodyAsString()
    override val underlyingKtorRequest: ApplicationRequest
        get() = call.request

    override fun bodyAsBytes() = bodyBytes
    override fun bodyAsString(): String = bodyBytes.decodeToString()
}

internal fun ApplicationCall.toMockzillaRequest(
    method: HttpMethod,
) = DefaultMockzillaHttpRequest(
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
