package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.testutils.runIntegrationTest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.Platform
import io.ktor.util.PlatformUtils
import io.ktor.util.platform
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
        if (PlatformUtils.platform is Platform.Js) {
            // Release mode not supported on JS
            return@runIntegrationTest
        }
        /* Run Test */
        val response = HttpClient().get("${params.apiBaseUrl}/meta")

        /* Verify */
        assertEquals(
            HttpStatusCode.Forbidden,
            response.status
        )
    }

    @Test
    fun `GET meta - with header - does not error`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .setIsReleaseModeEnabled(true)
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient().get("${params.apiBaseUrl}/meta") {
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
