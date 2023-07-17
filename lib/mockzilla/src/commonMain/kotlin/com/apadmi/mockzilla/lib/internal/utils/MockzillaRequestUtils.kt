package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

internal suspend fun ApplicationCall.toMockzillaRequest(
    method: HttpMethod,
) = MockzillaHttpRequest(
    request.uri,
    request.headers.entries().associate { it.key to it.value.joinToString() },
    // Content length header is provided by the Ktor engine, does not depend on the client to supply it
    // so this should reliably supply the size of the body.
    if ((request.contentLength() ?: 0) > 0) receiveText() else "",
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
