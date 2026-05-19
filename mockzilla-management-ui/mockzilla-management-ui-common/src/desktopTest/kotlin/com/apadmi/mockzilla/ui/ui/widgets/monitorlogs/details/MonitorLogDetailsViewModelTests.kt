package com.apadmi.mockzilla.ui.ui.widgets.monitorlogs.details

import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.ViewDetails.Tab

import app.cash.turbine.test
import org.junit.Test

import kotlin.test.assertEquals

class MonitorLogDetailsViewModelTests : CoroutineTest() {
    private fun createSut() = MonitorLogDetailsViewModel(testScope.backgroundScope)

    @Test
    fun `default state has Response tab selected`() = runBlockingTest {
        /* Setup */
        val sut = createSut()

        /* Verify */
        assertEquals(
            MonitorLogDetailsViewModel.State.ViewDetails(selectedTab = Tab.Response),
            sut.state.value,
        )
    }

    @Test
    fun `onTabSelected updates the selected tab`() = runBlockingTest {
        /* Setup */
        val sut = createSut()

        /* Run Test */
        sut.state.test {
            awaitItem()  // consume initial state

            sut.onTabSelected(Tab.Request)

            /* Verify */
            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(selectedTab = Tab.Request),
                awaitItem(),
            )

            sut.onTabSelected(Tab.Response)

            assertEquals(
                MonitorLogDetailsViewModel.State.ViewDetails(selectedTab = Tab.Response),
                awaitItem(),
            )
        }
    }
}
