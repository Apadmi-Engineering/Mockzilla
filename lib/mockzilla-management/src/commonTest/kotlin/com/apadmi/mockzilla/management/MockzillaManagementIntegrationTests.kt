package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.testutils.runIntegrationTest
import io.ktor.client.utils.HttpRequestCreated
import io.ktor.http.HttpStatusCode
import testutils.dummyEmpty

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
                    MonitorLogsResponse.dummyEmpty()
                ),
                actual = result
            )
        }

    @Test
    fun `fetchMonitorLogsAndClearBuffer with network calls- returns Monitor Logs`() =


        runIntegrationTest(
            dummyAppName,
            dummyAppVersion,
            config = MockzillaConfig.Builder()
                .setPort(0)
                .setFailureProbabilityPercentage(0)
                .setMeanDelayMillis(24)
                .setDelayVarianceMillis(0)
                .addEndpoint(
                    endpoint = EndpointConfiguration.Builder("my-id")
                        .setDefaultHandler {
                            MockzillaHttpResponse(
                                statusCode = HttpStatusCode.Created,
                                headers = mapOf("test" to "another test"),
                                body = "body"
                            )
                        }.build()
                ).build()
        ) { sut, connection, _ ->

            /* Setup */






            /* Run Test */
            val result = sut.fetchMonitorLogsAndClearBuffer(connection)

            /* Verify */
            assertEquals(
                expected = Result.success(
                    MonitorLogsResponse.dummyEmpty()
                ),
                actual = result
            )

        }
}