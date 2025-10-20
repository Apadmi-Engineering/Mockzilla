@file:Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")

package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import com.apadmi.mockzilla.testutils.runIntegrationTest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateServiceIntegrationTests {
    private val dummyConfig = EndpointConfiguration.Builder("Id").build()

    private suspend fun getEndpointConfig(
        connection: MockzillaConnectionConfig
    ) = MockzillaManagementRepositoryImpl.create(Logger.SIMPLE)
        .fetchAllEndpointConfigs(connection)
        .getOrThrow()
        .first { it.key == dummyConfig.key }

    @Test
    fun `setShouldFail - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val resultTrue = sut.setShouldFail(connection, listOf(dummyConfig.key), true).let {
                getEndpointConfig(connection)
            }
            val resultFalse = sut.setShouldFail(connection, listOf(dummyConfig.key), false).let {
                getEndpointConfig(connection)
            }

            /* Verify */
            assertEquals(preUpdate.copy(shouldFail = true), resultTrue)
            assertEquals(preUpdate.copy(shouldFail = false), resultFalse)
        }

    @Test
    fun `setDelay - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setDelay(connection, listOf(dummyConfig.key), 14872)
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(delayMs = 14872), postUpdate)
        }

    @Test
    fun `applyPreset - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.applyPreset(
                connection, dummyConfig.key, DashboardOverridePreset(
                    name = "name",
                    description = "description",
                    response = PartialMockzillaHttpResponse(
                        HttpStatusCode.Conflict,
                        mapOf("key" to "value"),
                        body = "body"
                    ),
                    type = null
                )
            )
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(
                preUpdate.copy(
                    defaultStatus = HttpStatusCode.Conflict,
                    defaultHeaders = mapOf("key" to "value"),
                    defaultBody = "body",
                    appliedPresetOverride = DashboardOverridePreset(
                        name = "name",
                        description = "description",
                        response = PartialMockzillaHttpResponse(
                            HttpStatusCode.Conflict,
                            mapOf("key" to "value"),
                            body = "body"
                        ),
                        type = null
                    )
                ), postUpdate
            )
        }
}
