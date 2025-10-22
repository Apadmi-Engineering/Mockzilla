package com.apadmi.mockzilla.ui.ui.widgets.endpoints.details

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel.*

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
        endpointsService = endpointsServiceMock,
        updateService = updateServiceMock,
        clearingService = clearingServiceMock,
        eventBus = eventBusMock,
        scope = testScope.backgroundScope
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
            appliedPresetOverride = null
        )
        val presets = DashboardOptionsConfig.Builder().build()

        every { eventBusMock.events }.returns(emptyFlow())

        coEvery {
            endpointsServiceMock.fetchAllEndpointConfigs(dummyActiveDevice)
        }.returns(Result.success(listOf(config)))

        coEvery {
            endpointsServiceMock.fetchDashboardOptionsConfig(dummyActiveDevice, dummyKey)
        }.returns(Result.success(presets))

        /* Run Test */
        val sut = createSut()
        val initialState = sut.state.value
        yield()

        /* Verify */
        assertEquals(State.Empty, initialState)
        assertEquals(
            State.Endpoint(
                config = config,
                fail = false,
                delayMillis = 50,
                isLoading = false,
                presets = State.Endpoint.Presets(
                    appliedPreset = null,
                    visiblePresets = presets.presets,
                    allPresets = presets.presets,
                    filter = ""
                ),
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

        every {
            eventBusMock.send(
                EventBus.Event.EndpointDataChanged(listOf(EndpointConfiguration.Key(raw = "key")))
            )
        }.returns(Unit)

        coEvery {
            updateServiceMock.setShouldFail(
                dummyActiveDevice, listOf(config.key), true
            )
        }.returns(Result.success(Unit))

        coEvery {
            updateServiceMock.applyPreset(
                dummyActiveDevice, config.key, any()
            )
        }.returns(Result.success(Unit))

        every { eventBusMock.events }.returns(emptyFlow())

        coEvery {
            endpointsServiceMock.fetchAllEndpointConfigs(dummyActiveDevice)
        }.returns(Result.success(listOf(config)))

        coEvery {
            endpointsServiceMock.fetchDashboardOptionsConfig(
                dummyActiveDevice, EndpointConfiguration.Key(dummyKey)
            )
        }.returns(Result.success(presets))

        /* Run Test */
        val sut = createSut()
        yield()
        val initialState = sut.state.value
        repeat(10) { yield() }

        sut.onPresetSelected(
            DashboardOverridePreset(
                name = "Preset name",
                description = null,
                type = DashboardOverridePreset.Type.Informational,
                response = PartialMockzillaHttpResponse(body = "hello")
            )
        )
        yield()
        sut.updateLatency(100)
        yield()
        yield()
        sut.onFailChange(true)
        yield()

        /* Verify */
        assertEquals(
            State.Endpoint(
                config = config,
                fail = null,
                delayMillis = null,
                isLoading = false,
                presets = State.Endpoint.Presets(
                    appliedPreset = null,
                    visiblePresets = presets.presets,
                    allPresets = presets.presets,
                    filter = ""
                ),
            ),
            initialState
        )

        assertEquals(
            State.Endpoint(
                config = config,
                fail = true,
                delayMillis = 100,
                isLoading = true,
                presets = State.Endpoint.Presets(
                    appliedPreset = DashboardOverridePreset(
                        name = "Preset name",
                        description = null,
                        type = DashboardOverridePreset.Type.Informational,
                        response = PartialMockzillaHttpResponse(body = "hello"),
                    ),
                    visiblePresets = presets.presets,
                    allPresets = presets.presets,
                    filter = ""
                ),
            ),
            sut.state.value
        )
    }
}
