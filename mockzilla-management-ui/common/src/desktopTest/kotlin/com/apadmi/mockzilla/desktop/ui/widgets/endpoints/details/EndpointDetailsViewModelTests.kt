package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.SelectedDeviceMonitoringViewModelBaseTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import io.ktor.http.HttpStatusCode
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("TOO_LONG_FUNCTION", "MAGIC_NUMBER")
class EndpointDetailsViewModelTests : SelectedDeviceMonitoringViewModelBaseTest() {
    @Mock
    private val endpointsServiceMock = mock(classOf<MockzillaManagement.EndpointsService>())

    @Mock
    private val updateServiceMock = mock(classOf<MockzillaManagement.UpdateService>())

    @Mock
    private val clearingServiceMock = mock(classOf<MockzillaManagement.CacheClearingService>())

    private fun createSut() = EndpointDetailsViewModel(
        key = EndpointConfiguration.Key("key"),
        endpointsServiceMock,
        updateServiceMock,
        clearingServiceMock,
        activeDeviceMonitorMock,
        testScope.backgroundScope
    )

    @Test
    fun `reloadData - pulls latest data from monitor and updates state`() = runBlockingTest {
        /* Setup */
        val dummyActiveDevice = Device.dummy().copy(ip = "device1")
        val dummyKey = EndpointConfiguration.Key("key")
        val dummyName = "foo"
        val dummyVersion = 1
        val config = SerializableEndpointConfig(
            key = dummyKey,
            name = dummyName,
            versionCode = dummyVersion,
            shouldFail = false,
            delayMs = 50,
            defaultHeaders = mapOf(),
            defaultBody = "{}",
            defaultStatus = HttpStatusCode.OK,
            errorHeaders = mapOf(),
            errorBody = "{}",
            errorStatus = HttpStatusCode.BadRequest,
        )
        val presets = DashboardOptionsConfig.Builder().build()
        given(endpointsServiceMock)
            .coroutine { fetchAllEndpointConfigs(dummyActiveDevice) }
            .thenReturn(Result.success(listOf(config)))
        given(endpointsServiceMock)
            .coroutine {
                fetchDashboardOptionsConfig(dummyActiveDevice, dummyKey)
            }
            .thenReturn(Result.success(presets))

        /* Run Test */
        val sut = createSut()
        val initialState = sut.state.value
        sut.reloadData(dummyActiveDevice)

        /* Verify */
        assertEquals(EndpointDetailsViewModel.State.Empty, initialState)
        assertEquals(
            EndpointDetailsViewModel.State.Endpoint(
                config = config,
                defaultBody = "{}",
                defaultStatus = HttpStatusCode.OK,
                defaultHeaders = listOf(),
                errorBody = "{}",
                errorStatus = HttpStatusCode.BadRequest,
                errorHeaders = listOf(),
                fail = false,
                delayMillis = "50",
                jsonEditingDefault = true,
                jsonEditingError = true,
                error = null,
                presets = presets,
            ),
            sut.state.value
        )
    }

    @Test
    fun `state is updated piecemeal by methods`() = runBlockingTest {
        /* Setup */
        selectedDeviceMock.value = StatefulDevice(Device.dummy(), "", true, "")
        val dummyKey = "key"
        val dummyName = "foo"
        val dummyVersion = 1
        val config = SerializableEndpointConfig.allNulls(
            key = dummyKey,
            name = dummyName,
            versionCode = dummyVersion
        )
        val presets = DashboardOptionsConfig.Builder().build()
        given(endpointsServiceMock)
            .coroutine { fetchAllEndpointConfigs(selectedDeviceMock.value!!.device) }
            .thenReturn(Result.success(listOf(config)))
        given(endpointsServiceMock)
            .coroutine {
                fetchDashboardOptionsConfig(selectedDeviceMock.value!!.device, EndpointConfiguration.Key(dummyKey))
            }
            .thenReturn(Result.success(presets))

        /* Run Test */
        val sut = createSut()
        sut.reloadData(selectedDeviceMock.value!!.device)
        val initialState = sut.state.value
        sut.onDefaultBodyChange("not json")
        sut.onDefaultStatusChange(HttpStatusCode.Accepted)
        sut.onDelayChange("100")
        sut.onErrorBodyChange("unauthorized")
        sut.onErrorStatusChange(HttpStatusCode.Unauthorized)
        sut.onFailChange(true)
        sut.onJsonDefaultEditingChange(false)
        sut.onJsonErrorEditingChange(false)
        sut.onDefaultHeadersChange(listOf("" to ""))
        sut.onErrorHeadersChange(listOf())

        /* Verify */
        assertEquals(
            EndpointDetailsViewModel.State.Endpoint(
                config = config,
                defaultBody = null,
                defaultStatus = null,
                defaultHeaders = null,
                errorBody = null,
                errorStatus = null,
                errorHeaders = null,
                fail = null,
                delayMillis = null,
                jsonEditingDefault = true,
                jsonEditingError = true,
                error = null,
                presets = presets,
            ),
            initialState
        )
        assertEquals(
            EndpointDetailsViewModel.State.Endpoint(
                config = config,
                defaultBody = "not json",
                defaultStatus = HttpStatusCode.Accepted,
                defaultHeaders = listOf("" to ""),
                errorBody = "unauthorized",
                errorStatus = HttpStatusCode.Unauthorized,
                errorHeaders = listOf(),
                fail = true,
                delayMillis = "100",
                jsonEditingDefault = false,
                jsonEditingError = false,
                error = null,
                presets = presets,
            ),
            sut.state.value
        )
    }
}
