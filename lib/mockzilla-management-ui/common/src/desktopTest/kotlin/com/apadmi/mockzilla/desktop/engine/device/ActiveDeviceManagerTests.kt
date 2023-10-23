package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import junit.framework.TestCase.assertEquals
import org.junit.Test

import kotlin.test.assertFalse
import kotlin.test.assertNull

class ActiveDeviceManagerTests : CoroutineTest() {
    @Mock
    private val metaDataUseCaseMock = mock(classOf<MetaDataUseCase>())

    private fun createSut() = ActiveDeviceManagerImpl(
        metaDataUseCaseMock,
        testScope
    )

    @Test
    fun `updateActiveDevice - updates device and notifies listeners`() = runBlockingTest {
        /* Setup */
        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy()))
        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.updateSelectedDevice(Device.dummy())

            /* Verify */
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `setActiveDeviceWithMetaData - updates device and notifies listeners`() = runBlockingTest {
        /* Setup */
        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy()))
        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(
                Device.dummy(),
                MetaData.dummy().copy(
                    appPackage = "test.package",
                    operatingSystem = "os",
                    deviceModel = "model"
                )
            )

            /* Verify */
            assertEquals(
                listOf(
                    StatefulDevice(
                        device = Device.dummy(),
                        name = "os-model",
                        isConnected = true,
                        connectedAppPackage = "test.package"
                    )
                ),
                sut.allDevices.toList()
            )
            assertEquals(Unit, awaitItem())  // Initial set
            assertEquals(Unit, awaitItem())  // When the polling registers change in app package
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `clearActiveDevice - clears notifies listeners`() = runBlockingTest {
        /* Setup */
        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.clearSelectedDevice()

            /* Verify */
            assertNull(sut.activeDevice)
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `monitorDeviceConnections - app package changes - notifies device change listeners`() = runBlockingTest {
        /* Setup */
        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy().copy(appPackage = "new.package")))

        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy().copy(appPackage = "old.package"))

            /* Verify */
            assertEquals(Unit, awaitItem())
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `monitorDeviceConnections - app package the same - does not notify device change listeners`() = runBlockingTest {
        /* Setup */
        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy()))
        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Verify */
            skipItems(1)  // One event for setting active device
            expectNoEvents()
            sut.cancelPolling()
        }
    }

    @Test
    fun `monitorDeviceConnections - fails to get metadata - updates connection status`() = runBlockingTest {
        /* Setup */
        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.failure(Exception()))
        val sut = createSut()

        sut.onDeviceConnectionStateChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Verify */
            assertEquals(Unit, awaitItem())
            assertFalse(sut.allDevices.first().isConnected)
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }
}
