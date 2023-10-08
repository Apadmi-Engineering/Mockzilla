package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.testutils.runIntegrationTest

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.encodeToString

@Suppress(
    "TOO_LONG_FUNCTION",
    "MAGIC_NUMBER",
    "TOO_MANY_LINES_IN_LAMBDA"
)
class MetaDataIntegrationTests {
    private val metaData = MetaData(
        "MyApplication",
        "com.example.example",
        "Os Version",
        "device model",
        "app version",
        "os",
        "version"
    )

    @Test
    fun `GET api-meta - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .build(),
        metaData = metaData
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/meta")

        /* Verify */
        assertEquals(
            JsonProvider.json.encodeToString(metaData),
            response.bodyAsText()
        )
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
    }
}
