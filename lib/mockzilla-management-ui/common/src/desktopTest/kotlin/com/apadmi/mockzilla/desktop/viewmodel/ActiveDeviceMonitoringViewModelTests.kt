package com.apadmi.mockzilla.desktop.viewmodel

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.testutils.CoroutineTest

import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.yield

@Suppress("MAGIC_NUMBER")
class ActiveDeviceMonitoringViewModelTests : CoroutineTest() {
    @Mock
    private val activeDeviceMonitorMock = mock(classOf<ActiveDeviceMonitor>())

    @Test
    fun `onDeviceSelectionChange fires should call reloadData`() = runBlockingTest {
        /* Setup */
        val numberOfEmits = 10
        given(activeDeviceMonitorMock).invocation { onDeviceSelectionChange }.thenReturn(flow<Unit> {
            repeat(numberOfEmits) {
                emit(Unit)
            }
        })

        /* Run Test */
        val sut = ConcreteDeviceMonitoringViewModel(activeDeviceMonitorMock, this)

        /* Verify */

        // Wait for each emission to be processed
        repeat(numberOfEmits) { yield() }
        // Expect an invocation for each emission plus one for the initial load.
        assertEquals(numberOfEmits + 1, sut.reloadDataInvocationCount)
    }
}

private class ConcreteDeviceMonitoringViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    var reloadDataInvocationCount = 0
    override suspend fun reloadData() {
        reloadDataInvocationCount++
    }
}
