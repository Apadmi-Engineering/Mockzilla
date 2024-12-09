package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.yield

class MonitorLogsViewModelTests : CoroutineTest() {
    private val dummyActiveDevice = Device.dummy()
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
    lateinit var monitorLogsUseCase: MonitorLogsUseCase

    private fun createSut() = MonitorLogsViewModel(
        dummyActiveDevice, monitorLogsUseCase, testScope.backgroundScope
    )

    @Test
    fun `init - pulls latest data from monitor and updates state`() = runBlockingTest {
        /* Setup */
        coEvery { monitorLogsUseCase.getMonitorLogs(dummyActiveDevice) }
            .returns(Result.success(sequenceOf(dummyLogEvent)))
        val sut = createSut()

        /* Run Test */
        sut.state.test {
            /* Verify */
            assertEquals(
                listOf(),
                awaitItem().entries.toList()
            )
            assertEquals(
                listOf(dummyLogEvent),
                awaitItem().entries.toList()
            )
        }
    }

    @Test
    fun `clearLogs calls the use case and updates state`() = runBlockingTest {
        /* Setup */
        var logs = arrayOf(dummyLogEvent)
        coEvery { monitorLogsUseCase.getMonitorLogs(dummyActiveDevice) }
            .answers { Result.success(sequenceOf(*logs)) }
        coEvery { monitorLogsUseCase.clearMonitorLogs(dummyActiveDevice) }.answers {
            logs = arrayOf()
            Result.success(Unit)
        }
        val sut = createSut()

        /* Run Test */
        yield()
        sut.state.test {
            sut.clearLogs()

            /* Verify */
            assertEquals(
                listOf(dummyLogEvent),
                awaitItem().entries.toList()
            )
            assertEquals(
                listOf(),
                awaitItem().entries.toList()
            )
        }
    }
}
