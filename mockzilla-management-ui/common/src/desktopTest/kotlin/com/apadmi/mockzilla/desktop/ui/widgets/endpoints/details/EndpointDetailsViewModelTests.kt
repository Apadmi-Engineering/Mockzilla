package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import io.ktor.http.HttpStatusCode
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.yield

@Suppress("TOO_LONG_FUNCTION", "MAGIC_NUMBER")
class EndpointDetailsViewModelTests : CoroutineTest() {
    @Mock
    private val endpointsServiceMock = mock(classOf<MockzillaManagement.EndpointsService>())

    @Mock
    private val updateServiceMock = mock(classOf<MockzillaManagement.UpdateService>())

    @Mock
    private val clearingServiceMock = mock(classOf<MockzillaManagement.CacheClearingService>())

    @Mock
    private val eventBusMock = mock(classOf<EventBus>())
    private val dummyActiveDevice = Device.dummy().copy(ip = "device1")

    private fun createSut() = EndpointDetailsViewModel(
        key = EndpointConfiguration.Key("key"),
        device = dummyActiveDevice,
        endpointsServiceMock,
        updateServiceMock,
        clearingServiceMock,
        eventBusMock,
        testScope.backgroundScope
    )

    @Test
    fun `init - pulls latest data from monitor and updates state`() = runBlockingTest {
        /* Setup */
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
        given(eventBusMock).invocation { events }.thenReturn(emptyFlow())
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
        yield()

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
        val dummyKey = "key"
        val dummyName = "foo"
        val dummyVersion = 1
        val config = SerializableEndpointConfig.allNulls(
            key = dummyKey,
            name = dummyName,
            versionCode = dummyVersion
        )
        val presets = DashboardOptionsConfig.Builder().build()
        given(updateServiceMock).coroutine {
            setDefaultBody(
                dummyActiveDevice,
                config.key,
                "not json"
            )
        }.thenReturn(Result.success(Unit))
        given(eventBusMock).invocation {
            send(EventBus.Event.EndpointDataChanged(listOf(EndpointConfiguration.Key(raw = "key"))))
        }.thenReturn(Unit)
        given(updateServiceMock).coroutine {
            setErrorStatus(dummyActiveDevice, config.key, HttpStatusCode.Unauthorized)
        }.thenReturn(Result.success(Unit))
        given(updateServiceMock).coroutine {
            setShouldFail(dummyActiveDevice, listOf(config.key), true)
        }.thenReturn(Result.success(Unit))
        given(updateServiceMock).coroutine {
            setDefaultStatus(dummyActiveDevice, config.key, HttpStatusCode.Accepted)
        }.thenReturn(Result.success(Unit))
        given(updateServiceMock).coroutine {
            setErrorBody(dummyActiveDevice, config.key, "unauthorized")
        }.thenReturn(Result.success(Unit))
        given(updateServiceMock).coroutine {
            setDefaultHeaders(dummyActiveDevice, config.key, listOf("" to "").toMap())
        }.thenReturn(Result.success(Unit))
        given(updateServiceMock).coroutine {
            setErrorHeaders(dummyActiveDevice, config.key, listOf("" to "").toMap())
        }.thenReturn(Result.success(Unit))
        given(eventBusMock).invocation { events }.thenReturn(emptyFlow())
        given(endpointsServiceMock)
            .coroutine { fetchAllEndpointConfigs(dummyActiveDevice) }
            .thenReturn(Result.success(listOf(config)))
        given(endpointsServiceMock)
            .coroutine {
                fetchDashboardOptionsConfig(dummyActiveDevice, EndpointConfiguration.Key(dummyKey))
            }
            .thenReturn(Result.success(presets))

        /* Run Test */
        val sut = createSut()
        yield()
        val initialState = sut.state.value
        repeat(10) { yield() }

        sut.onDefaultBodyChange("not json")
        yield()
        sut.onDefaultStatusChange(HttpStatusCode.Accepted)
        yield()
        sut.onDelayChange("100")
        yield()
        sut.onErrorBodyChange("unauthorized")
        yield()
        sut.onErrorStatusChange(HttpStatusCode.Unauthorized)
        yield()
        sut.onFailChange(true)
        yield()
        sut.onJsonDefaultEditingChange(false)
        yield()
        sut.onJsonErrorEditingChange(false)
        yield()
        sut.onDefaultHeadersChange(listOf("" to ""))
        yield()
        sut.onErrorHeadersChange(listOf())
        yield()

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
