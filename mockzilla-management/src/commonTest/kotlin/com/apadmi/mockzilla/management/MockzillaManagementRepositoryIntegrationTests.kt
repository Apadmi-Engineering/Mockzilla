@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.testutils.runIntegrationTest

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockzillaManagementRepositoryIntegrationTests {
    private val dummyAppName = "MockzillaManagementTest"
    private val dummyAppVersion = "0.0.0-test"
    private val fetchLogsAndClearBufferEndpoint = "clear-endpoint"

    // the mock log
    private val mockLog = LogEvent(
        timestamp = 0,
        url = "/local-mock/$fetchLogsAndClearBufferEndpoint",
        requestBody = "",
        requestHeaders = emptyMap(),
        responseBody = "",
        responseHeaders = emptyMap(),
        status = HttpStatusCode.Created,
        delay = 24,
        method = "GET",
        isIntendedFailure = false
    )

    @Test
    fun `fetchMetaData - returns metadata`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it }
        ) { sut, connection, runtimeParams ->
            /* Run Test */
            val result = sut.fetchMetaData(connection)

            /* Verify */
            assertEquals(
                Result.success(
                    MetaData(
                        appName = dummyAppName,
                        appPackage = "-",
                        operatingSystemVersion = System.getProperty("os.version"),
                        deviceModel = "-",
                        appVersion = dummyAppVersion,
                        operatingSystem = System.getProperty("os.name"),
                        mockzillaVersion = runtimeParams.mockzillaVersion
                    )
                ), result
            )
        }

    @Test
    fun `fetchMonitorLogsAndClearBuffer - returns empty Monitor Logs`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it }) { sut, connection, _ ->
            /* Run Test */
            val result = sut.fetchMonitorLogsAndClearBuffer(connection)

            /* Verify */
            assertEquals(
                expected = Result.success(
                    MonitorLogsResponse(
                        appPackage = "-", logs = emptyList()
                    )
                ),
                actual = result
            )
        }

    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `fetchAllMockData and updateMockData - behaves somewhat sensibly`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it },
            config = MockzillaConfig.Builder().setPort(0).addEndpoint(
                EndpointConfiguration.Builder("Id")
                    .setDefaultHandler { MockzillaHttpResponse(body = "this is a body") }
                    .setShouldFail(false)
                    .setMeanDelayMillis(10)
                    .build(),

            )
                .addEndpoint(
                    EndpointConfiguration.Builder("Id 2")
                        .setDefaultHandler { MockzillaHttpResponse(body = "this is another body") }
                        .setShouldFail(false)
                        .build()
                ).setLogLevel(MockzillaConfig.LogLevel.Verbose)
                .build()
        ) { sut, connection, _ ->
            /* Run Test */
            val preUpdateData = sut.fetchAllMockData(connection)
            val entryToUpdate = preUpdateData.getOrThrow().last()
            val updateResult = sut.updateMockDataEntry(
                SerializableEndpointConfigurationPatchRequestDto(
                    key = entryToUpdate.key,
                    name = entryToUpdate.name,
                    shouldFail = SetOrDont.Set(true)
                ), connection
            )
            val postUpdateData = sut.fetchAllMockData(connection)

            /* Verify */
            listOf(preUpdateData, updateResult, postUpdateData).forEach { assertTrue(it.isSuccess) }
            assertEquals(
                listOf(false, false), preUpdateData.getOrThrow().map { it.shouldFail }
            )
            assertEquals(
                listOf(false, true),
                postUpdateData.getOrThrow().map { it.shouldFail }
            )
        }

    @Suppress("MAGIC_NUMBER")
    @Test
    fun `fetchMonitorLogsAndClearBuffer with network calls- returns list of logs`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it },
            config = MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .setDelayMillis(24)
                .addEndpoint(
                    EndpointConfiguration.Builder(fetchLogsAndClearBufferEndpoint)
                        .setDefaultHandler {
                            MockzillaHttpResponse(
                                statusCode = HttpStatusCode.Created,
                                body = ""
                            )
                        }.build()
                )
                .build()
        ) { sut, connection, params ->

            /* Setup */

            // Make a request for network logs
            HttpClient(CIO)
                .get(urlString = "${params.mockBaseUrl}/$fetchLogsAndClearBufferEndpoint")

            val expectedLogs = MonitorLogsResponse(appPackage = "-", listOf(mockLog))

            /* Run Test */
            val actualLogs = sut.fetchMonitorLogsAndClearBuffer(connection).getOrNull()!!

            /* Verify */
            assertEquals(
                expected = expectedLogs,
                actual = actualLogs.copy(logs = actualLogs.logs.map {
                    it.copy(
                        timestamp = 0,
                        requestHeaders = emptyMap()
                    )
                })
            )
        }
}
