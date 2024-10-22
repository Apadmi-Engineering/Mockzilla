package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

class MonitorLogsUseCaseImplTests : CoroutineTest() {
    private val dummyLogEvent = LogEvent(
        timestamp = 1,
        url = "www.example.com",
        requestBody = "",
        requestHeaders = mapOf(),
        responseHeaders = mapOf(),
        responseBody = "",
        status = HttpStatusCode.OK,
        delay = 1,
        method = "GET",
        isIntendedFailure = false
    )

    @RelaxedMockK
    lateinit var managementLogsService: MockzillaManagement.LogsService

    @RelaxedMockK
    lateinit var managementMetaDataService: MockzillaManagement.MetaDataService

    private fun createSut() = MonitorLogsUseCaseImpl(
        managementLogsService, managementMetaDataService
    )

    @Test
    fun `getMonitorLogs fails - returns failure`() = runBlockingTest {
        /* Setup */
        coEvery {
            managementLogsService.fetchMonitorLogsAndClearBuffer(
                Device.dummy(),
                hideFromLogs = true
            )
        }.returns(Result.failure(Exception()))
        val sut = createSut()

        /* Run Test */
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertTrue(result.isFailure)
    }

    @Test
    fun `getMonitorLogs success - returns and combines with cache`() = runBlockingTest {
        /* Setup */
        coEvery {
            managementLogsService.fetchMonitorLogsAndClearBuffer(
                Device.dummy(),
                hideFromLogs = true
            )
        }.returnsMany(
            Result.success(
                MonitorLogsResponse(
                    appPackage = "package",
                    logs = listOf(dummyLogEvent)
                )
            ),
            Result.success(
                MonitorLogsResponse(
                    appPackage = "package",
                    logs = listOf(dummyLogEvent.copy(url = "https://www.example.com"))
                )
            )
        )
        val sut = createSut()

        /* Run Test */
        val result1 = sut.getMonitorLogs(Device.dummy())
        val result2 = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertEquals(
            listOf(dummyLogEvent), result1.getOrThrow().toList()
        )
        assertEquals(
            listOf(dummyLogEvent, dummyLogEvent.copy(url = "https://www.example.com")),
            result2.getOrThrow().toList()
        )
    }

    @Test
    fun `clearMonitorLogs success - removes cache`() = runBlockingTest {
        /* Setup */
        coEvery {
            managementLogsService.fetchMonitorLogsAndClearBuffer(
                Device.dummy(),
                hideFromLogs = true
            )
        }.returnsMany(
            Result.success(
                MonitorLogsResponse(
                    appPackage = "package",
                    logs = listOf(dummyLogEvent)
                )
            ),
            Result.success(
                MonitorLogsResponse(
                    appPackage = "package",
                    logs = listOf()
                )
            )
        )
        coEvery { managementMetaDataService.fetchMetaData(Device.dummy(), hideFromLogs = true) }
            .returns(Result.success(MetaData.dummy().copy(appPackage = "package")))
        val sut = createSut()

        /* Run Test */
        sut.getMonitorLogs(Device.dummy())
        sut.clearMonitorLogs(Device.dummy())
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertEquals(
            listOf<LogEvent>(),
            result.getOrThrow().toList()
        )
    }

    @Test
    fun `clearMonitorLogs fails - returns failure`() = runBlockingTest {
        /* Setup */
        coEvery { managementMetaDataService.fetchMetaData(Device.dummy(), hideFromLogs = true) }
            .returns(Result.failure(Exception()))
        val sut = createSut()

        /* Run Test */
        val result = sut.clearMonitorLogs(Device.dummy())

        /* Verify */
        assertTrue(result.isFailure)
    }
}
