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

import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.yield

@Suppress("TOO_LONG_FUNCTION", "MAGIC_NUMBER")
class EndpointDetailsViewModelTests : CoroutineTest() {
    private val dummyActiveDevice = Device.dummy().copy(ip = "device1")

    @RelaxedMockK
    lateinit var endpointsServiceMock: MockzillaManagement.EndpointsService

    @RelaxedMockK
    lateinit var updateServiceMock: MockzillaManagement.UpdateService

    @RelaxedMockK
    lateinit var clearingServiceMock: MockzillaManagement.CacheClearingService

    @RelaxedMockK
    lateinit var eventBusMock: EventBus

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
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(dummyActiveDevice) }
            .returns(Result.success(listOf(config)))
        coEvery { endpointsServiceMock.fetchDashboardOptionsConfig(dummyActiveDevice, dummyKey) }
            .returns(Result.success(presets))

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
        coEvery {
            updateServiceMock.setDefaultBody(
                dummyActiveDevice,
                config.key,
                "not json"
            )
        }.returns(Result.success(Unit))
        every { eventBusMock.send(EventBus.Event.EndpointDataChanged(listOf(EndpointConfiguration.Key(raw = "key")))) }
            .returns(Unit)
        coEvery { updateServiceMock.setErrorStatus(dummyActiveDevice, config.key, HttpStatusCode.Unauthorized) }
            .returns(Result.success(Unit))
        coEvery { updateServiceMock.setShouldFail(dummyActiveDevice, listOf(config.key), true) }
            .returns(Result.success(Unit))
        coEvery { updateServiceMock.setDefaultStatus(dummyActiveDevice, config.key, HttpStatusCode.Accepted) }
            .returns(Result.success(Unit))
        coEvery { updateServiceMock.setErrorBody(dummyActiveDevice, config.key, "unauthorized") }
            .returns(Result.success(Unit))
        coEvery { updateServiceMock.setDefaultHeaders(dummyActiveDevice, config.key, listOf("" to "").toMap()) }
            .returns(Result.success(Unit))
        coEvery { updateServiceMock.setErrorHeaders(dummyActiveDevice, config.key, listOf("" to "").toMap()) }
            .returns(Result.success(Unit))
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(dummyActiveDevice) }
            .returns(Result.success(listOf(config)))
        coEvery { endpointsServiceMock.fetchDashboardOptionsConfig(dummyActiveDevice, EndpointConfiguration.Key(dummyKey)) }
            .returns(Result.success(presets))

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
                jsonEditingDefault = false,
                jsonEditingError = false,
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
                presets = presets,
            ),
            sut.state.value
        )
    }
}
