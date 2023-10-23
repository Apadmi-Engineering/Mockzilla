package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.runIntegrationTest
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
    fun `fetchMonitorLogsAndClearBuffer - returns Monitor Logs with buffer clear`() =
        runIntegrationTest(dummyAppName, dummyAppVersion) { sut, connection, _ ->
            /* Run Test */
            val result = sut.fetchMonitorLogsAndClearBuffer(connection)

            /* Verify */
            assertEquals(
                expected = Result.success(
                    MonitorLogsResponse(
                        appPackage = "package",
                        logs = listOf(
                            LogEvent(
                                timestamp = 1234,
                                url = "url",
                                requestBody = "body",
                                requestHeaders = mapOf("1" to "request1", "2" to "request2"),
                                responseHeaders = mapOf("1" to "response1", "2" to "request2"),
                                responseBody = "body",
                                status = HttpStatusCode.Accepted,
                                delay = 1000,
                                method = "method",
                                isIntendedFailure = false,
                            )
                        ),
                    )
                ),
                actual = result
            )

        }
}
