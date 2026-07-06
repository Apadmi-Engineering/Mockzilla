@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.engine.device

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
    lateinit var metaDataUseCase: MetaDataUseCase

    private fun createSut() = MonitorLogsUseCaseImpl(
        managementLogsService, metaDataUseCase
    )

    private fun givenOldServer() {
        coEvery { metaDataUseCase.getMetaData(Device.dummy()) }
            .returns(Result.success(MetaData.dummy().copy(mockzillaVersion = "1.0.0")))
    }

    private fun givenNewServer() {
        coEvery { metaDataUseCase.getMetaData(Device.dummy()) }
            .returns(Result.success(MetaData.dummy().copy(mockzillaVersion = "4.0.0")))
    }

    // ===== Old path (server < 4.0.0) =====

    @Test
    fun `getMonitorLogs old path - fails - returns failure`() = runBlockingTest {
        /* Setup */
        givenOldServer()
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
    fun `getMonitorLogs old path - success - returns and combines with cache`() = runBlockingTest {
        /* Setup */
        givenOldServer()
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
            listOf(dummyLogEvent), result1.getOrThrow().logs
        )
        assertEquals(
            listOf(dummyLogEvent, dummyLogEvent.copy(url = "https://www.example.com")),
            result2.getOrThrow().logs
        )
    }

    @Test
    fun `clearMonitorLogs old path - success - removes cache`() = runBlockingTest {
        /* Setup */
        givenOldServer()
        coEvery {
            managementLogsService.fetchMonitorLogsAndClearBuffer(
                Device.dummy(),
                hideFromLogs = true
            )
        }.returnsMany(
            Result.success(
                MonitorLogsResponse(
                    appPackage = MetaData.dummy().appPackage,
                    logs = listOf(dummyLogEvent)
                )
            ),
            Result.success(
                MonitorLogsResponse(
                    appPackage = MetaData.dummy().appPackage,
                    logs = listOf()
                )
            )
        )
        val sut = createSut()

        /* Run Test */
        sut.getMonitorLogs(Device.dummy())
        sut.clearMonitorLogs(Device.dummy())
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertEquals(listOf<LogEvent>(), result.getOrThrow().logs)
    }

    @Test
    fun `clearMonitorLogs old path - metaData fails - returns failure`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCase.getMetaData(Device.dummy()) }
            .returns(Result.failure(Exception()))
        val sut = createSut()

        /* Run Test */
        val result = sut.clearMonitorLogs(Device.dummy())

        /* Verify */
        assertTrue(result.isFailure)
    }

    // ===== New path (server >= 4.0.0) =====

    @Test
    fun `getMonitorLogs new path - first poll returns all entries`() = runBlockingTest {
        /* Setup */
        givenNewServer()
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), null, any())
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(dummyLogEvent))))
        val sut = createSut()

        /* Run Test */
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertEquals(listOf(dummyLogEvent), result.getOrThrow().logs)
    }

    @Test
    fun `getMonitorLogs new path - subsequent poll uses since cursor`() = runBlockingTest {
        /* Setup */
        givenNewServer()
        val secondEvent = dummyLogEvent.copy(timestamp = 2, id = "other-id", url = "https://www.other.com")
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), null, any())
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(dummyLogEvent))))
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), 0L, any())  // last.timestamp - 1 = 1 - 1 = 0
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(secondEvent))))
        val sut = createSut()

        /* Run Test */
        sut.getMonitorLogs(Device.dummy())
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertEquals(listOf(dummyLogEvent, secondEvent), result.getOrThrow().logs)
    }

    @Test
    fun `getMonitorLogs new path - deduplicates by id`() = runBlockingTest {
        /* Setup */
        givenNewServer()
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), null, any())
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(dummyLogEvent))))
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), 0L, any())
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(dummyLogEvent))))
        val sut = createSut()

        /* Run Test */
        sut.getMonitorLogs(Device.dummy())
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify — same event returned in both polls should only appear once */
        assertEquals(listOf(dummyLogEvent), result.getOrThrow().logs)
    }

    @Test
    fun `getMonitorLogs new path - sorts by timestamp`() = runBlockingTest {
        /* Setup */
        givenNewServer()
        val eventAtT3 = dummyLogEvent.copy(timestamp = 3, id = "first")
        val eventAtT1 = dummyLogEvent.copy(timestamp = 1, url = "other")
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), null, any())
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(eventAtT3, eventAtT1))))
        val sut = createSut()

        /* Run Test */
        val result = sut.getMonitorLogs(Device.dummy())

        /* Verify */
        assertEquals(listOf(eventAtT1, eventAtT3), result.getOrThrow().logs)
    }

    @Test
    fun `clearMonitorLogs new path - calls deleteMonitorLogs and clears cache`() = runBlockingTest {
        /* Setup */
        givenNewServer()
        coEvery {
            managementLogsService.fetchMonitorLogsSince(Device.dummy(), null, any())
        }.returns(Result.success(MonitorLogsResponse(appPackage = "pkg", logs = listOf(dummyLogEvent))))
        coEvery { managementLogsService.deleteMonitorLogs(Device.dummy()) }
            .returns(Result.success(Unit))
        val sut = createSut()

        /* Run Test */
        sut.getMonitorLogs(Device.dummy())
        val clearResult = sut.clearMonitorLogs(Device.dummy())

        /* Verify */
        assertTrue(clearResult.isSuccess)
    }

    @Test
    fun `fetchLogDetail new path - delegates to fetchFullBodyLogDetail`() = runBlockingTest {
        /* Setup */
        givenNewServer()
        coEvery { managementLogsService.fetchFullBodyLogDetail(Device.dummy(), dummyLogEvent.id) }
            .returns(Result.success(dummyLogEvent))
        val sut = createSut()

        /* Run Test */
        val result = sut.fetchLogDetail(Device.dummy(), dummyLogEvent.id)

        /* Verify */
        assertEquals(dummyLogEvent, result.getOrThrow())
    }

    @Test
    fun `fetchLogDetail old path - returns failure`() = runBlockingTest {
        /* Setup */
        givenOldServer()
        val sut = createSut()

        /* Run Test */
        val result = sut.fetchLogDetail(Device.dummy(), dummyLogEvent.id)

        /* Verify */
        assertTrue(result.isFailure)
    }
}
