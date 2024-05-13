@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
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
            val resultTrue = sut.setShouldFail(connection, dummyConfig.key, true)
            val resultFalse = sut.setShouldFail(connection, dummyConfig.key, false)

            /* Verify */
            assertEquals(preUpdate.copy(shouldFail = true), resultTrue.getOrThrow())
            assertEquals(preUpdate.copy(shouldFail = false), resultFalse.getOrThrow())
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
            val result = sut.setDelay(connection, dummyConfig.key, 14872)

            /* Verify */
            assertEquals(preUpdate.copy(delayMs = 14872), result.getOrThrow())
        }

    @Test
    fun `setDefaultHeaders - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setDefaultHeaders(connection, dummyConfig.key, mapOf("my test header" to "a test value"))

            /* Verify */
            assertEquals(preUpdate.copy(defaultHeaders = mapOf("my test header" to "a test value")), result.getOrThrow())
        }

    @Test
    fun `setErrorHeaders - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setErrorHeaders(connection, dummyConfig.key, mapOf("my test header" to "a test value"))

            /* Verify */
            assertEquals(preUpdate.copy(errorHeaders = mapOf("my test header" to "a test value")), result.getOrThrow())
        }

    @Test
    fun `setDefaultBody - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setDefaultBody(connection, dummyConfig.key, "my test body")

            /* Verify */
            assertEquals(preUpdate.copy(defaultBody = "my test body"), result.getOrThrow())
        }

    @Test
    fun `setDefaultStatus - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setDefaultStatus(connection, dummyConfig.key, HttpStatusCode.Conflict)

            /* Verify */
            assertEquals(preUpdate.copy(defaultStatus = HttpStatusCode.Conflict), result.getOrThrow())
        }

    @Test
    fun `setErrorBody - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setErrorBody(connection, dummyConfig.key, "my test body")

            /* Verify */
            assertEquals(preUpdate.copy(errorBody = "my test body"), result.getOrThrow())
        }

    @Test
    fun `setErrorStatus - performs update`() =
        runIntegrationTest(
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(dummyConfig)
                .build(),
            createSut = { UpdateServiceImpl(it) }
        ) { sut, connection, _ ->
            /* Setup */
            val preUpdate = getEndpointConfig(connection)

            /* Run Test */
            val result = sut.setErrorStatus(connection, dummyConfig.key, HttpStatusCode.Conflict)

            /* Verify */
            assertEquals(preUpdate.copy(errorStatus = HttpStatusCode.Conflict), result.getOrThrow())
        }
}
