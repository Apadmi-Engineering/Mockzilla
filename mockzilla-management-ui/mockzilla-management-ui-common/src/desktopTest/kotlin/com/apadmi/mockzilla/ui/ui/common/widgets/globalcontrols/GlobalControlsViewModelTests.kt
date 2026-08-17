@file:Suppress("TOO_LONG_FUNCTION", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsViewModel.*

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.yield

class GlobalControlsViewModelTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var endpointsServiceMock: MockzillaManagement.EndpointsService

    @RelaxedMockK
    lateinit var updateServiceMock: MockzillaManagement.UpdateService

    @RelaxedMockK
    lateinit var eventBusMock: EventBus

    @RelaxedMockK
    lateinit var clearingServiceMock: MockzillaManagement.CacheClearingService

    private fun createSut() = GlobalControlsViewModel(
        device = Device.dummy(),
        endpointsService = endpointsServiceMock,
        updateService = updateServiceMock,
        clearingService = clearingServiceMock,
        eventBus = eventBusMock,
        testScope.backgroundScope,
    )

    private fun List<SerializableEndpointConfig>.toState() = map { config ->
        EndpointsViewModel.State.EndpointConfig(
            key = config.key,
            name = config.name,
            fail = config.shouldFail == true,
            overriddenProperties = listOfNotNull(
                EndpointProperties.Delay.takeIf { _ -> config.delayMs != null },
                EndpointProperties.Body.takeIf { _ -> config.appliedPresetOverride?.response?.body != null },
                EndpointProperties.Status.takeIf { _ -> config.appliedPresetOverride?.response?.statusCode != null },
                EndpointProperties.Headers.takeIf { _ -> config.appliedPresetOverride?.response?.headers != null }
            ),
            delayMs = config.delayMs,
        )
    }

    @Test
    fun `reloadData - partial failure - populates correctly`() = runBlockingTest {
        /* Setup */
        val partialFailure = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = true),
            SerializableEndpointConfig.allNulls("Key3", "Name3", 1)
                .copy(shouldFail = true, delayMs = 10),
        )

        val fullFailureAndLatency = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1)
                .copy(shouldFail = true, delayMs = 10),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = true, delayMs = 10),
            SerializableEndpointConfig.allNulls("Key3", "Name3", 1)
                .copy(shouldFail = true, delayMs = 10),
        )

        val noFailure = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1)
                .copy(shouldFail = false),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = false, delayMs = 10),
            SerializableEndpointConfig.allNulls("Key3", "Name3", 1)
                .copy(shouldFail = false, delayMs = 10),
        )

        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returnsMany(
                Result.success(fullFailureAndLatency),
                Result.success(partialFailure),
                Result.success(noFailure)
            )
        val sut = createSut()
        sut.state.test {
            /* Run Test */
            assertEquals(State.Loading, awaitItem())
            assertEquals(
                State.Idle(
                    initialLatencyMs = 10,
                    apiFailureState = ForceFailureBannerState.FullFailure,
                    isLoading = false,
                    endpoints = fullFailureAndLatency.toState(),
                    activeOverridesCount = 3
                ), awaitItem()
            )

            sut.reloadData()
            assertEquals(
                State.Idle(
                    initialLatencyMs = null,
                    apiFailureState = ForceFailureBannerState.PartialFailure,
                    isLoading = false,
                    endpoints = partialFailure.toState(),
                    activeOverridesCount = 2
                ), awaitItem()
            )

            sut.reloadData()
            assertEquals(
                State.Idle(
                    initialLatencyMs = null,
                    apiFailureState = ForceFailureBannerState.Normal,
                    isLoading = false,
                    endpoints = noFailure.toState(),
                    activeOverridesCount = 2
                ), awaitItem()
            )
        }
    }

    @Test
    fun `restoreApi - updates correctly`() = runBlockingTest {
        /* Setup */
        val endpoints = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = true),
        )
        val endpointKeys = endpoints.map { it.key }
        every { eventBusMock.send(EventBus.Event.EndpointDataChanged(endpointKeys)) }
            .returns(Unit)
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { updateServiceMock.setShouldFail(Device.dummy(), endpointKeys, any()) }
            .returns(Result.success(Unit))
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(endpoints))
        val sut = createSut()
        sut.state.test {
            skipItems(2)
            yield()

            /* Run Test */
            sut.restoreApi()

            /* Verify */
            assertEquals(State.Idle(
                initialLatencyMs = null,
                apiFailureState = ForceFailureBannerState.PartialFailure,
                isLoading = true,
                endpoints = endpoints.toState(),
                activeOverridesCount = 1
            ), awaitItem())
            coVerify {
                updateServiceMock.setShouldFail(Device.dummy(), endpointKeys, false)
            }
        }
    }

    @Test
    fun `forceFailure - updates correctly`() = runBlockingTest {
        /* Setup */
        val endpoints = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = true),
        )
        val endpointKeys = endpoints.map { it.key }
        every { eventBusMock.send(EventBus.Event.EndpointDataChanged(endpointKeys)) }
            .returns(Unit)
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { updateServiceMock.setShouldFail(Device.dummy(), endpointKeys, any()) }
            .returns(Result.success(Unit))
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(endpoints))
        val sut = createSut()
        sut.state.test {
            skipItems(2)
            yield()

            /* Run Test */
            sut.forceFailure()

            /* Verify */
            assertEquals(State.Idle(
                initialLatencyMs = null,
                apiFailureState = ForceFailureBannerState.PartialFailure,
                isLoading = true,
                endpoints = endpoints.toState(),
                activeOverridesCount = 1
            ), awaitItem())
            coVerify {
                updateServiceMock.setShouldFail(Device.dummy(), endpointKeys, true)
            }
        }
    }

    @Test
    fun `resetLatency - updates correctly`() = runBlockingTest {
        /* Setup */
        val endpoints = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = true),
        )
        val endpointKeys = endpoints.map { it.key }
        every { eventBusMock.send(EventBus.Event.EndpointDataChanged(endpointKeys)) }
            .returns(Unit)
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { updateServiceMock.setDelay(Device.dummy(), endpointKeys, any()) }
            .returns(Result.success(Unit))
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(endpoints))
        val sut = createSut()
        sut.state.test {
            skipItems(2)
            yield()

            /* Run Test */
            sut.resetLatency()

            /* Verify */
            assertEquals(State.Idle(
                initialLatencyMs = null,
                apiFailureState = ForceFailureBannerState.PartialFailure,
                isLoading = true,
                endpoints = endpoints.toState(),
                activeOverridesCount = 1
            ), awaitItem())
            coVerify {
                updateServiceMock.setDelay(Device.dummy(), endpointKeys, null)
            }
        }
    }

    @Test
    fun `updateLatency - updates correctly`() = runBlockingTest {
        /* Setup */
        val endpoints = listOf(
            SerializableEndpointConfig.allNulls("Key1", "Name1", 1),
            SerializableEndpointConfig.allNulls("Key2", "Name2", 1)
                .copy(shouldFail = true),
        )
        val endpointKeys = endpoints.map { it.key }
        every { eventBusMock.send(EventBus.Event.EndpointDataChanged(endpointKeys)) }
            .returns(Unit)
        every { eventBusMock.events }.returns(emptyFlow())
        coEvery { updateServiceMock.setDelay(Device.dummy(), endpointKeys, any()) }
            .returns(Result.success(Unit))
        coEvery { endpointsServiceMock.fetchAllEndpointConfigs(Device.dummy()) }
            .returns(Result.success(endpoints))
        val sut = createSut()
        sut.state.test {
            skipItems(2)
            yield()

            /* Run Test */
            sut.updateLatency(1592)

            /* Verify */
            assertEquals(State.Idle(
                initialLatencyMs = null,
                apiFailureState = ForceFailureBannerState.PartialFailure,
                isLoading = true,
                endpoints = endpoints.toState(),
                activeOverridesCount = 1
            ), awaitItem())
            coVerify {
                updateServiceMock.setDelay(Device.dummy(), endpointKeys, 1592)
            }
        }
    }
}
