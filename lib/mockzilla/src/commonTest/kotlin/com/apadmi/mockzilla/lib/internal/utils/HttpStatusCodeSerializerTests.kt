package com.apadmi.mockzilla.lib.internal.utils

import io.ktor.http.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HttpStatusCodeSerializerTests {
    @Test
    fun `encode and decode - works correctly`() {
        /* Setup */
        val dummy = DummyContainer(HttpStatusCode.BadRequest)

        /* Run Test */
        val encoded = Json.encodeToString(dummy)
        val decoded: DummyContainer = Json.decodeFromString(encoded)

        /* Verify */
        assertEquals("""{"status":400}""", encoded)
        assertEquals(dummy, decoded)
    }

    /**
     * @property status
     */
    @Serializable
    private data class DummyContainer(
        @Serializable(with = HttpStatusCodeSerializer::class)
        val status: HttpStatusCode
    )
}
