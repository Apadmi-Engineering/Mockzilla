package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class MockzillaConfigValidatorTests {
    @Test
    fun `no endpoints - throws`() {
        runTest(
            MockzillaConfig.Builder().build(),
            "Config must contain at least 1 endpoint"
        )
    }

    @Test
    fun `duplicate keys - throws`() {
        runTest(
            MockzillaConfig.Builder()
                .addEndpoint(EndpointConfiguration.Builder("id"))
                .addEndpoint(EndpointConfiguration.Builder("id"))
                .build(),
            "Endpoints must have unique keys"
        )
    }

    @Test
    fun `blank keys - throws`() {
        runTest(
            MockzillaConfig.Builder()
                .addEndpoint(EndpointConfiguration.Builder(" "))
                .build(),
            "Endpoints must have non blank keys"
        )
    }

    @Test
    fun `negative delay- throws`() {
        runTest(
            MockzillaConfig.Builder()
                .addEndpoint(EndpointConfiguration
                    .Builder("id")
                    .setMeanDelayMillis(-1)
                )
                .build(),
            "Delay mean must be in range 0 to ${Int.MAX_VALUE / 2 - 1}"
        )
    }


    @Test
    fun `delay too large - throws`() {
        runTest(
            MockzillaConfig.Builder()
                .addEndpoint(EndpointConfiguration
                    .Builder("id")
                    .setMeanDelayMillis(Int.MAX_VALUE / 2 + 10)
                )
                .build(),
            "Delay mean must be in range 0 to ${Int.MAX_VALUE / 2 - 1}"
        )
    }

    @Test
    fun `invalid port - throws`() {
        runTest(
            MockzillaConfig.Builder()
                .setPort(-1)
                .build(),
            "Port cannot be negative"
        )
    }

    private fun runTest(config: MockzillaConfig, expectedMessage: String) {
        val result = assertFails {
            config.validate()
        }

        assertEquals("Invalid Config: $expectedMessage", result.message)
    }
}
