@file:Suppress("KDOC_NO_CONSTRUCTOR_PROPERTY")

package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import io.ktor.http.HttpMethod

internal class TestMockzillaHttpRequest(
    override val uri: String = "",
    override val headers: Map<String, String> = emptyMap(),
    val body: String = "",
    override val method: HttpMethod = HttpMethod.Get
) : MockzillaHttpRequest {
    override suspend fun bodyAsBytes() = body.encodeToByteArray()
    override suspend fun bodyAsString() = body
}
