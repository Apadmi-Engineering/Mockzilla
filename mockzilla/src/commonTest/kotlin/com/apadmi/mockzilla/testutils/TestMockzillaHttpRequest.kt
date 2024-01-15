package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import io.ktor.http.HttpMethod
import io.ktor.server.request.ApplicationRequest

internal class TestMockzillaHttpRequest(
    override val uri: String = "",
    override val headers: Map<String, String> = emptyMap(),
    @Deprecated("`body`is deprecated", replaceWith = ReplaceWith("bodyAsString()"))
    override val body: String = "",
    override val method: HttpMethod = HttpMethod.Get
) : MockzillaHttpRequest {
    override val underlyingKtorRequest: ApplicationRequest
        get() = throw NotImplementedError("This is a fake request, it does not have an underlying ktor request")

    override fun bodyAsBytes() = body.encodeToByteArray()
    override fun bodyAsString() = body
}
