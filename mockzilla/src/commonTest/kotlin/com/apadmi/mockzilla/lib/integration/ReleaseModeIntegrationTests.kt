package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.testutils.runIntegrationTest
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ReleaseModeIntegrationTests {
    @Test
    fun `GET meta - without header - errors`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .setIsReleaseModeEnabled(true)
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/meta")

        /* Verify */
        assertEquals(
            HttpStatusCode.Forbidden,
            response.status
        )
    }

    @Test
    fun `GET meta - with header - errors`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .setIsReleaseModeEnabled(true)
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/meta") {
            params.authHeaderProvider.generateHeader().also { header ->
                headers.append(header.key, header.value)
            }
        }

        /* Verify */
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
    }
}
