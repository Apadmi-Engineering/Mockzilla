package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device

import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Before

import kotlinx.coroutines.flow.flowOf

abstract class ActiveDeviceMonitoringViewModelBaseTest : CoroutineTest() {
    @Mock
    val activeDeviceMonitorMock = mock(classOf<ActiveDeviceMonitor>())
    var activeDeviceMock: Device? = null

    @Before
    fun setup() {
        given(activeDeviceMonitorMock).invocation { activeDevice }.then { activeDeviceMock }
        given(activeDeviceMonitorMock).invocation { onDeviceSelectionChange }.thenReturn(flowOf())
    }
}
