package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel.*
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test

import io.mockk.coEvery
import io.mockk.every
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertFalse

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
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
                isCheckboxTicked = false,
                hasValuesOverridden = false,
                display = true
            ),
            State.EndpointConfig(
                key = EndpointConfiguration.Key("Key2"),
                name = "Name2",
                fail = true,
                isCheckboxTicked = false,
                hasValuesOverridden = false,
                display = true
            ),
            State.EndpointConfig(
                key = EndpointConfiguration.Key("Key3"),
                name = "Name3",
                fail = true,
                isCheckboxTicked = false,
                hasValuesOverridden = true,
                display = true
            )
        ),
        filter = ""
    )

    @RelaxedMockK
    lateinit var endpointsServiceMock: MockzillaManagement.EndpointsService

    @RelaxedMockK
    lateinit var updateServiceMock: MockzillaManagement.UpdateService

    @RelaxedMockK
    lateinit var eventBusMock: EventBus

    private fun createSut() = EndpointsViewModel(
        Device.dummy(),
        endpointsServiceMock,
        updateServiceMock,
        eventBusMock,
        testScope.backgroundScope
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
    fun `onCheckboxChanged - updates correctly`() = runBlockingTest {
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
            skipItems(2)
            yield()

            /* Run Test */
            sut.onCheckboxChanged(EndpointConfiguration.Key("Key1"), true)
            val result1 = awaitItem()
            sut.onCheckboxChanged(EndpointConfiguration.Key("Key1"), false)
            val result2 = awaitItem()

            /* Verify */
            assertTrue((result1 as State.EndpointsList).endpoints.first { it.key.raw == "Key1" }.isCheckboxTicked)
            assertFalse((result2 as State.EndpointsList).endpoints.first { it.key.raw == "Key1" }.isCheckboxTicked)
        }
    }

    @Test
    fun `onAllCheckboxChanged - updates correctly`() = runBlockingTest {
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
            skipItems(2)
            yield()

            /* Run Test */
            sut.onAllCheckboxChanged(true)
            val result1 = awaitItem()
            sut.onAllCheckboxChanged(false)
            val result2 = awaitItem()

            /* Verify */
            assertTrue((result1 as State.EndpointsList).endpoints.all { it.isCheckboxTicked })
            assertFalse((result2 as State.EndpointsList).endpoints.all { it.isCheckboxTicked })
        }
    }

    @Test
    fun `onFailChanged - updates correctly`() = runBlockingTest {
        /* Setup */
        every { eventBusMock.send(EventBus.Event.EndpointDataChanged(listOf(EndpointConfiguration.Key(raw = "Key1")))) }
            .returns(Unit)
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { updateServiceMock.setShouldFail(Device.dummy(), listOf(EndpointConfiguration.Key("Key1")), true) }
            .returns(Result.success(Unit))
        coEvery { updateServiceMock.setShouldFail(Device.dummy(), listOf(EndpointConfiguration.Key("Key1")), false) }
            .returns(Result.success(Unit))
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
            skipItems(2)
            yield()

            /* Run Test */
            sut.onFailChanged(EndpointConfiguration.Key("Key1"), true)
            val result1 = awaitItem()
            sut.onFailChanged(EndpointConfiguration.Key("Key1"), false)
            val result2 = awaitItem()

            /* Verify */
            assertTrue((result1 as State.EndpointsList).endpoints.first { it.key.raw == "Key1" }.fail)
            assertFalse((result2 as State.EndpointsList).endpoints.first { it.key.raw == "Key1" }.fail)
        }
    }

    @Test
    fun `onFilterChanged - updates correctly`() = runBlockingTest {
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
                defaultEndpointList.copy(defaultEndpointList.endpoints.mapIndexed { index, value ->
                    value.copy(
                        display = index == 0
                    )
                }, filter = "Name1"), (result1 as State.EndpointsList)
            )

            assertEquals(
                defaultEndpointList.copy(defaultEndpointList.endpoints.mapIndexed { index, value ->
                    value.copy(
                        display = index == 1
                    )
                }, filter = "Name2"), (result2 as State.EndpointsList)
            )

            assertEquals(defaultEndpointList.copy(endpoints = defaultEndpointList.endpoints.map {
                it.copy(
                    display = false
                )
            }, filter = "Name4"), (result3 as State.EndpointsList))

            assertEquals(defaultEndpointList, (result4 as State.EndpointsList))
        }
    }
}
