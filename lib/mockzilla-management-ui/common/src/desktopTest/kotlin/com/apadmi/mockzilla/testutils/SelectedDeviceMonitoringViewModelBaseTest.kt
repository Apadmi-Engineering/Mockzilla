package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice

import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before

abstract class SelectedDeviceMonitoringViewModelBaseTest : CoroutineTest() {
    @Mock
    val activeDeviceMonitorMock = mock(classOf<ActiveDeviceMonitor>())
    var selectedDeviceMock = MutableStateFlow<StatefulDevice?>(null)

    @Before
    fun setup() {
        given(activeDeviceMonitorMock).invocation { selectedDevice }.then { selectedDeviceMock }
    }
}
