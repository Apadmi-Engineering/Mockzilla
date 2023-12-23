package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.runIntegrationTest

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
}
