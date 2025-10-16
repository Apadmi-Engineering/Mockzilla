package com.apadmi.mockzilla.ui.ui.widgets.endpoints.details

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel.*

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.yield

@Suppress("TOO_LONG_FUNCTION")
class EndpointsViewModelTests : CoroutineTest() {
    private val defaultEndpointList = State.EndpointsList(
        listOf(
            State.EndpointConfig(
                key = EndpointConfiguration.Key("Key1"),
                name = "Name1",
                fail = false,
                overriddenProperties = emptyList(),
                display = true
            ),
            State.EndpointConfig(
                key = EndpointConfiguration.Key("Key2"),
                name = "Name2",
                fail = true,
                overriddenProperties = emptyList(),
                display = true
            ),
            State.EndpointConfig(
                key = EndpointConfiguration.Key("Key3"),
                name = "Name3",
                fail = true,
                overriddenProperties = listOf(EndpointProperties.Delay),
                display = true
            )
        ),
        filter = ""
    )

    @RelaxedMockK
    lateinit var endpointsServiceMock: MockzillaManagement.EndpointsService

    @RelaxedMockK
    lateinit var eventBusMock: EventBus

    private fun createSut() = EndpointsViewModel(
        device = Device.dummy(),
        endpointsService = endpointsServiceMock,
        eventBus = eventBusMock,
        scope = testScope.backgroundScope
    )

    @Test
    fun `reloadData - populates correctly`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(
                Result.success(
                    listOf(
                        SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
                        SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                            .copy(shouldFail = true),
                        SerializableEndpointConfig.allNulls("Key3", "Name3", 1)
                            .copy(shouldFail = true, delayMs = 10),
                    )
                )
            )
        val sut = createSut()
        sut.state.test {
            /* Run Test */
            assertEquals(State.Loading, awaitItem())
            assertEquals(
                defaultEndpointList, awaitItem()
            )
        }
    }

    @Test
    fun `onFilterChanged - updates correctly`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery {
            endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy())
        }.returns(
            Result.success(
                listOf(
                    SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
                    SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                        .copy(shouldFail = true),
                    SerializableEndpointConfig.allNulls("Key3", "Name3", 1)
                        .copy(shouldFail = true, delayMs = 10),
                )
            )
        )

        val sut = createSut()
        sut.state.test {
            skipItems(2)
            yield()

            /* Run Test */
            sut.onFilterChanged("Name1")
            val result1 = awaitItem()

            sut.onFilterChanged("Name2")
            val result2 = awaitItem()

            sut.onFilterChanged("Name4")
            val result3 = awaitItem()

            sut.onFilterChanged("")
            val result4 = awaitItem()

            /* Verify */
            assertEquals(
                defaultEndpointList.copy(
                    endpoints = defaultEndpointList.endpoints.mapIndexed { index, value ->
                        value.copy(display = index == 0)
                    },
                    filter = "Name1"
                ),
                (result1 as State.EndpointsList)
            )

            assertEquals(
                defaultEndpointList.copy(
                    endpoints = defaultEndpointList.endpoints.mapIndexed { index, value ->
                        value.copy(display = index == 1)
                    },
                    filter = "Name2"
                ),
                (result2 as State.EndpointsList)
            )

            assertEquals(
                defaultEndpointList.copy(
                    endpoints = defaultEndpointList.endpoints.map {
                        it.copy(display = false)
                    },
                    filter = "Name4"
                ),
                (result3 as State.EndpointsList)
            )

            assertEquals(defaultEndpointList, (result4 as State.EndpointsList))
        }
    }
}
