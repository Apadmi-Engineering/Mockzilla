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
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(defaultHeaders = mapOf("my test header" to "a test value")), postUpdate)
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
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(errorHeaders = mapOf("my test header" to "a test value")), postUpdate)
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
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(defaultBody = "my test body"), postUpdate)
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
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(defaultStatus = HttpStatusCode.Conflict), postUpdate)
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
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(errorBody = "my test body"), postUpdate)
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
            val postUpdate = getEndpointConfig(connection)

            /* Verify */
            assertEquals(Result.success(Unit), result)
            assertEquals(preUpdate.copy(errorStatus = HttpStatusCode.Conflict), postUpdate)
        }
}
