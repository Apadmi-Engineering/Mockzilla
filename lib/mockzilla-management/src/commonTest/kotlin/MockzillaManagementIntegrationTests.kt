package com.apadmi.mockzilla

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MockzillaManagementIntegrationTests {

    private val dummyAppName = "MockzillaManagementTest"
    private val dummyAppVersion = "0.0.0-test"

    @Test
    fun `fetchMetaData - returns metadata`() =
        runIntegrationTest { sut, connection, runtimeParams ->
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

    private fun runIntegrationTest(
        config: MockzillaConfig = MockzillaConfig.Builder().setPort(0).addEndpoint(
            EndpointConfiguration.Builder("Id").build()
        ).build(),
        testBlock: suspend (
            sut: MockzillaManagement,
            connection: MockzillaManagement.ConnectionConfig,
            runtimeParams: MockzillaRuntimeParams
        ) -> Unit
    ) = runBlocking {
        /* Setup */
        val sut = MockzillaManagement.create()
        val runtimeParams = startMockzilla(dummyAppName, dummyAppVersion, config)

        /* Run Test */
        testBlock(
            sut,
            MockzillaManagement.ConnectionConfig("127.0.0.1", port = runtimeParams.port.toString()),
            runtimeParams
        )

        /* Cleanup */
        stopMockzilla()
    }
}