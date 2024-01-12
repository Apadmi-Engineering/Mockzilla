package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider

import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class HttpStatusCodeSerializerTests {
    @Test
    fun `encode and decode - works correctly`() {
        /* Setup */
        val dummy = DummyContainer(HttpStatusCode.BadRequest)

        /* Run Test */
        val encoded = JsonProvider.json.encodeToString(dummy)
        val decoded: DummyContainer = JsonProvider.json.decodeFromString(encoded)

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
