package com.apadmi.mockzilla.ui.ui.widgets.monitorlogs.details

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.BodyState
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.ViewDetails.Tab

import app.cash.turbine.test
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertIs

@Suppress("MAGIC_NUMBER")
internal class MonitorLogDetailsViewModelTests : CoroutineTest() {
    private val dummyDevice = Device.dummy()
    private val dummyLogEvent = LogEvent(
        id = "test-id",
        timestamp = 1L,
        url = "https://example.com",
        requestBody = "req-body",
        requestHeaders = emptyMap(),
        responseHeaders = emptyMap(),
        responseBody = "res-body",
        status = HttpStatusCode.OK,
        delay = 0,
        method = "GET",
        isIntendedFailure = false,
    )

    @RelaxedMockK
    lateinit var monitorLogsUseCase: MonitorLogsUseCase

    private fun createSut(logEvent: LogEvent? = dummyLogEvent) = MonitorLogDetailsViewModel(
        device = dummyDevice,
        logEvent = logEvent,
        monitorLogsUseCase = monitorLogsUseCase,
        scope = testScope.backgroundScope,
    )

    @Test
    fun `init - null logEvent - state is Empty`() = runBlockingTest {
        val sut = createSut(logEvent = null)
        assertEquals(MonitorLogDetailsViewModel.State.Empty, sut.state.value)
    }

    @Test
    fun `init - non-truncated bodies - state is Available immediately, no fetch triggered`() = runBlockingTest {
        val sut = createSut()
        val state = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(sut.state.value)
        assertIs<BodyState.Available>(state.requestBodyState)
        assertIs<BodyState.Available>(state.responseBodyState)
        assertEquals("req-body", (state.requestBodyState as BodyState.Available).text)
        assertEquals("res-body", (state.responseBodyState as BodyState.Available).text)
        coVerify(exactly = 0) { monitorLogsUseCase.fetchLogDetail(any(), any()) }
    }

    @Test
    fun `init - truncated request body - starts Loading then transitions to Available`() = runBlockingTest {
        val truncatedEvent = dummyLogEvent.copy(
            requestBody = "truncated-req",
            isRequestBodyTruncated = true,
        )
        val fullEvent = truncatedEvent.copy(
            requestBody = "full-request-body",
            isRequestBodyTruncated = false,
        )
        coEvery { monitorLogsUseCase.fetchLogDetail(dummyDevice, truncatedEvent.id) }
            .returns(Result.success(fullEvent))

        val sut = createSut(truncatedEvent)

        sut.state.test {
            val initial = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertIs<BodyState.Loading>(initial.requestBodyState)
            assertEquals("truncated-req", (initial.requestBodyState as BodyState.Loading).preview)

            val loaded = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertIs<BodyState.Available>(loaded.requestBodyState)
            assertEquals("full-request-body", (loaded.requestBodyState as BodyState.Available).text)
        }
    }

    @Test
    fun `init - truncated response body - starts Loading then transitions to Available`() = runBlockingTest {
        val truncatedEvent = dummyLogEvent.copy(
            responseBody = "truncated-res",
            isResponseBodyTruncated = true,
        )
        val fullEvent = truncatedEvent.copy(
            responseBody = "full-response-body",
            isResponseBodyTruncated = false,
        )
        coEvery { monitorLogsUseCase.fetchLogDetail(dummyDevice, truncatedEvent.id) }
            .returns(Result.success(fullEvent))

        val sut = createSut(truncatedEvent)

        sut.state.test {
            val initial = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertIs<BodyState.Loading>(initial.responseBodyState)

            val loaded = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertEquals("full-response-body", (loaded.responseBodyState as BodyState.Available).text)
        }
    }

    @Test
    fun `init - both bodies truncated - single fetch enriches both`() = runBlockingTest {
        val truncatedEvent = dummyLogEvent.copy(
            requestBody = "trunc-req",
            responseBody = "trunc-res",
            isRequestBodyTruncated = true,
            isResponseBodyTruncated = true,
        )
        val fullEvent = truncatedEvent.copy(
            requestBody = "full-req",
            responseBody = "full-res",
            isRequestBodyTruncated = false,
            isResponseBodyTruncated = false,
        )
        coEvery { monitorLogsUseCase.fetchLogDetail(dummyDevice, truncatedEvent.id) }
            .returns(Result.success(fullEvent))

        val sut = createSut(truncatedEvent)

        sut.state.test {
            awaitItem()  // Loading state
            val loaded = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertEquals("full-req", (loaded.requestBodyState as BodyState.Available).text)
            assertEquals("full-res", (loaded.responseBodyState as BodyState.Available).text)
        }

        coVerify(exactly = 1) { monitorLogsUseCase.fetchLogDetail(any(), any()) }
    }

    @Test
    fun `init - fetch fails - transitions to Error with preview text preserved`() = runBlockingTest {
        val truncatedEvent = dummyLogEvent.copy(
            requestBody = "trunc-req",
            responseBody = "trunc-res",
            isRequestBodyTruncated = true,
            isResponseBodyTruncated = true,
        )
        coEvery { monitorLogsUseCase.fetchLogDetail(dummyDevice, truncatedEvent.id) }
            .returns(Result.failure(Exception("network error")))

        val sut = createSut(truncatedEvent)

        sut.state.test {
            awaitItem()  // Loading state
            val errState = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            val reqState = assertIs<BodyState.Error>(errState.requestBodyState)
            val resState = assertIs<BodyState.Error>(errState.responseBodyState)
            assertEquals("trunc-req", reqState.preview)
            assertEquals("trunc-res", resState.preview)
        }
    }

    @Test
    fun `onTabSelected - updates selectedTab in ViewDetails state`() = runBlockingTest {
        val sut = createSut()

        sut.state.test {
            awaitItem()  // consume initial

            sut.onTabSelected(Tab.Request)
            val afterRequest = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertEquals(Tab.Request, afterRequest.selectedTab)

            sut.onTabSelected(Tab.Response)
            val afterResponse = assertIs<MonitorLogDetailsViewModel.State.ViewDetails>(awaitItem())
            assertEquals(Tab.Response, afterResponse.selectedTab)
        }
    }
}
