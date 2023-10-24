package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
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

class MockzillaManagementIntegrationTests {
    private val dummyAppName = "MockzillaManagementTest"
    private val dummyAppVersion = "0.0.0-test"

    @Test
    fun `fetchMetaData - returns metadata`() =
        runIntegrationTest(dummyAppName, dummyAppVersion) { sut, connection, runtimeParams ->
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
        runIntegrationTest(dummyAppName, dummyAppVersion) { sut, connection, _ ->
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


    private val fetchLogsAndClearBufferEndpoint = "clear-endpoint"

    //the mock log
    @Suppress("MAGIC_NUMBER")
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


    @Suppress("MAGIC_NUMBER")
    @Test
    fun `fetchMonitorLogsAndClearBuffer with network calls- returns list of logs`() =
        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            config = MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .setFailureProbabilityPercentage(0)
                .setMeanDelayMillis(24)
                .setDelayVarianceMillis(0)
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
