package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.details

import com.apadmi.mockzilla.testutils.CoroutineTest

import app.cash.turbine.test
import org.junit.Test

import kotlin.test.assertEquals

class MonitorLogDetailsViewModelTests : CoroutineTest() {
    private fun createSut() = MonitorLogDetailsViewModel(testScope.backgroundScope)

    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `onView methods update the state`() = runBlockingTest {
        /* Setup */
        val sut = createSut()

        /* Run Test */
        sut.state.test {
            sut.onViewRequestBody(true)
            sut.onViewRequestHeaders(true)
            sut.onViewResponseHeaders(true)
            sut.onViewResponseBody(false)

            /* Verify */
            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(
                    requestHeaders = false,
                    requestBody = false,
                    responseHeaders = false,
                    responseBody = true,
                ),
                awaitItem()
            )
            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(
                    requestHeaders = false,
                    requestBody = true,
                    responseHeaders = false,
                    responseBody = true,
                ),
                awaitItem()
            )
            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(
                    requestHeaders = true,
                    requestBody = true,
                    responseHeaders = false,
                    responseBody = true,
                ),
                awaitItem()
            )
            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(
                    requestHeaders = true,
                    requestBody = true,
                    responseHeaders = true,
                    responseBody = true,
                ),
                awaitItem()
            )
            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(
                    requestHeaders = true,
                    requestBody = true,
                    responseHeaders = true,
                    responseBody = false,
                ),
                awaitItem()
            )
        }
    }
}
