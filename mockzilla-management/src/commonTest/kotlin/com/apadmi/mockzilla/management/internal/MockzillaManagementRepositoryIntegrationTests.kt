@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.runIntegrationTest

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TOO_LONG_FUNCTION")
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
            val result = sut.fetchMetaData(connection, false)

            /* Verify */
            assertEquals(

                MetaData(
                    appName = dummyAppName,
                    appPackage = "-",
                    operatingSystemVersion = System.getProperty("os.version"),
                    deviceModel = "-",
                    appVersion = dummyAppVersion,
                    runTarget = RunTarget.IosDevice,
                    mockzillaVersion = runtimeParams.mockzillaVersion
                ), result.getOrThrow().copy(runTarget = RunTarget.IosDevice)
            )
        }

    @Test
    fun `fetchMonitorLogsAndClearBuffer - returns empty Monitor Logs`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it }) { sut, connection, _ ->
            /* Run Test */
            val result = sut.fetchMonitorLogsAndClearBuffer(connection, false)

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

    @Test
    fun `fetchAllEndpointConfigs and updateMockData - behaves somewhat sensibly`() =
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
            val preUpdateData = sut.fetchAllEndpointConfigs(connection)
            val entryToUpdate = preUpdateData.getOrThrow().last()
            val updateResult = sut.updateMockDataEntry(
                SerializableEndpointPatchItemDto(
                    key = entryToUpdate.key,
                    shouldFail = SetOrDont.Set(true)
                ), connection
            )
            val postUpdateData = sut.fetchAllEndpointConfigs(connection)

            /* Verify */
            listOf(preUpdateData, updateResult, postUpdateData).forEach { assertTrue(it.isSuccess) }
            assertEquals(
                listOf(null, null), preUpdateData.getOrThrow().map { it.shouldFail }
            )
            assertEquals(
                listOf(null, true),
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
            val actualLogs = sut.fetchMonitorLogsAndClearBuffer(connection, false).getOrNull()!!

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

    @Test
    fun `fetchDashboardOptionsConfig - returns config`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it as MockzillaManagement.EndpointsService },
            config = MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .addEndpoint(
                    EndpointConfiguration.Builder("my key")
                        .configureDashboardOverrides {
                            addSuccessPreset(MockzillaHttpResponse(body = "preset"), "name", "desc")
                        }.build()
                )
                .build()
        ) { sut, connection, _ ->
            /* Run Test */
            val result =
                sut.fetchDashboardOptionsConfig(connection, EndpointConfiguration.Key("my key"))

            /* Verify */
            assertEquals(
                DashboardOptionsConfig(
                    successPresets = listOf(
                        DashboardOverridePreset(
                            "name",
                            "desc",
                            MockzillaHttpResponse(body = "preset")
                        )
                    ),
                    errorPresets = emptyList()
                ), result.getOrThrow()
            )
        }

    @Test
    fun `clearCaches and clearAllCaches - behave correctly`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            createSut = { it },
            config = MockzillaConfig.Builder().setPort(0)
                .addEndpoint(EndpointConfiguration.Builder("Id"))
                .addEndpoint(EndpointConfiguration.Builder("Id 2"))
                .setLogLevel(MockzillaConfig.LogLevel.Verbose)
                .build()
        ) { sut, connection, _ ->
            /* Run Test */
            // Populate Cache
            sut.updateMockDataEntries(
                listOf(
                    SerializableEndpointPatchItemDto(
                        key = EndpointConfiguration.Key("Id"),
                        shouldFail = SetOrDont.Set(true)
                    ),
                    SerializableEndpointPatchItemDto(
                        key = EndpointConfiguration.Key("Id 2"),
                        shouldFail = SetOrDont.Set(true)
                    )
                ), connection
            )
            // Check cache was populated
            check(
                sut.fetchAllEndpointConfigs(connection)
                    .getOrThrow()
                    .map { it.shouldFail } == listOf(true, true)
            )

            /* Run Test */
            sut.clearCaches(connection, listOf(EndpointConfiguration.Key("Id")))
            val afterClearingOneCache = sut.fetchAllEndpointConfigs(connection)
            sut.clearAllCaches(connection)
            val afterClearingAllCaches = sut.fetchAllEndpointConfigs(connection)

            /* Verify */
            assertEquals(
                listOf(
                    SerializableEndpointConfig.allNulls("Id", "Id", Int.MIN_VALUE),
                    SerializableEndpointConfig.allNulls("Id 2", "Id 2", Int.MIN_VALUE)
                        .copy(shouldFail = true),
                ),
                afterClearingOneCache.getOrThrow()
            )
            assertEquals(
                listOf(
                    SerializableEndpointConfig.allNulls("Id", "Id", Int.MIN_VALUE),
                    SerializableEndpointConfig.allNulls("Id 2", "Id 2", Int.MIN_VALUE),
                ),
                afterClearingAllCaches.getOrThrow()
            )
        }
}
