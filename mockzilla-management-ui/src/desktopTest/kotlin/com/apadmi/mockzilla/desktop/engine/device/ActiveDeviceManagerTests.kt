package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test

import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import org.junit.Test

import kotlin.test.assertFalse
import kotlin.test.assertNull

class ActiveDeviceManagerTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var metaDataUseCaseMock: MetaDataUseCase

    private fun createSut() = ActiveDeviceManagerImpl(
        metaDataUseCaseMock,
        testScope
    )

    @Test
    fun `updateActiveDevice - updates device and notifies listeners`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(any(), any()) }.returns(Result.success(MetaData.dummy()))

        val sut = createSut()

        sut.selectedDevice.test {
            skipItems(1)
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Run Test */
            sut.updateSelectedDevice(Device.dummy())

            /* Verify */
            assertEquals(Device.dummy(), awaitItem()?.device)
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `setActiveDeviceWithMetaData - updates device and notifies listeners`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy(), true) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        sut.selectedDevice.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(
                Device.dummy(),
                MetaData.dummy().copy(
                    appPackage = "test.package",
                    runTarget = RunTarget.Jvm,
                    mockzillaVersion = "99.99.99",
                    deviceModel = "model"
                )
            )

            /* Verify */
            assertEquals(
                listOf(
                    StatefulDevice(
                        device = Device.dummy(),
                        name = "Jvm-model",
                        isConnected = true,
                        connectedAppPackage = "test.package",
                        isCompatibleMockzillaVersion = true
                    )
                ),
                sut.allDevices.toList()
            )
            assertNull(awaitItem())
            assertEquals(Device.dummy(), awaitItem()?.device)

            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `setActiveDeviceWithMetaData - incompatible version`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy(), true) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        sut.selectedDevice.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(
                Device.dummy(),
                MetaData.dummy().copy(mockzillaVersion = "0.0.0")
            )

            /* Verify */
            assertFalse(sut.allDevices.first().isCompatibleMockzillaVersion)
            cancelAndIgnoreRemainingEvents()
            sut.cancelPolling()
        }
    }

    @Test
    fun `clearActiveDevice - clears notifies listeners`() = runBlockingTest {
        /* Setup */
        val sut = createSut()

        sut.selectedDevice.test {
            /* Run Test */
            sut.clearSelectedDevice()

            /* Verify */
            assertNull(awaitItem())
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `monitorDeviceConnections - app package changes - notifies device change listeners`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy(), true) }.returns(
            Result.success(MetaData.dummy().copy(appPackage = "new.package"))
        )

        val sut = createSut()

        sut.selectedDevice.test {
            skipItems(1)
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy().copy(appPackage = "old.package"))

            /* Verify */
            awaitItem().apply {
                assertEquals(Device.dummy(), this?.device)
                assertEquals("old.package", this?.connectedAppPackage)
            }
            awaitItem().apply {
                assertEquals(Device.dummy(), this?.device)
                assertEquals("new.package", this?.connectedAppPackage)
            }
            ensureAllEventsConsumed()
            sut.cancelPolling()
        }
    }

    @Test
    fun `monitorDeviceConnections - app package the same - does not notify device change listeners`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy(), true) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        sut.selectedDevice.test {
            skipItems(1)
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
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy(), true) }.returns(
            Result.failure(Exception())
        )
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
