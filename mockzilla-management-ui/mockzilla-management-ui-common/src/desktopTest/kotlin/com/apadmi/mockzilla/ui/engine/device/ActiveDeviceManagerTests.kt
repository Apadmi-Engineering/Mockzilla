package com.apadmi.mockzilla.ui.engine.device

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
import kotlin.test.assertTrue

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
        coEvery { metaDataUseCaseMock.getMetaData(any()) }.returns(Result.success(MetaData.dummy()))

        val sut = createSut()

        sut.selectedDevice.test {
            skipItems(1)
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Run Test */
            sut.updateSelectedDevice(Device.dummy())

            /* Verify */
            assertEquals(Device.dummy(), awaitItem()?.device)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `setActiveDeviceWithMetaData - updates device and notifies listeners`() = runBlockingTest {
        /* Setup */
        val metaDataFixture = MetaData.dummy().copy(
            appPackage = "test.package",
            runTarget = RunTarget.Jvm,
            mockzillaVersion = "99.99.99",
            deviceModel = "model"
        )
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        sut.selectedDevice.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(
                Device.dummy(),
                metaDataFixture
            )

            /* Verify */
            assertEquals(
                listOf(
                    StatefulDevice(
                        device = Device.dummy(),
                        metaData = metaDataFixture,
                        isConnected = true,
                        isCompatibleMockzillaVersion = true
                    )
                ),
                sut.allDevices.toList()
            )
            assertNull(awaitItem())
            assertEquals(Device.dummy(), awaitItem()?.device)

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `setActiveDeviceWithMetaData - incompatible version`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
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
        }
    }

    @Test
    fun `removeDevice - device not selected - emits state change`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())
        sut.clearSelectedDevice()

        sut.onDeviceConnectionStateChange.test {
            /* Run test */
            sut.removeDevice(Device.dummy())

            /* Verify */
            skipItems(1)
            awaitItem()
            assertTrue { sut.allDevices.isEmpty() }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `removeDevice - device selected - emits state change, clears selected device`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())
        sut.clearSelectedDevice()

        sut.onDeviceConnectionStateChange.test {
            awaitItem()

            /* Run test */
            sut.removeDevice(Device.dummy())

            /* Verify */
            awaitItem()
            assertTrue { sut.allDevices.isEmpty() }
            assertNull(sut.selectedDevice.value)

            /* Tear down */
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onLogPollSuccess - app package changes - re-fetches metadata and notifies listeners`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
            Result.success(MetaData.dummy().copy(appPackage = "new.package"))
        )
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy().copy(appPackage = "old.package"))

        sut.selectedDevice.test {
            awaitItem()  // consume initial setActiveDeviceWithMetaData emission

            /* Run Test */
            sut.onLogPollSuccess(Device.dummy(), "new.package")

            /* Verify */
            awaitItem().apply {
                assertEquals(Device.dummy(), this?.device)
                assertEquals("new.package", this?.metaData?.appPackage)
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onLogPollSuccess - app package the same and connected - does not notify listeners`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

        sut.selectedDevice.test {
            awaitItem()  // consume initial emission

            /* Run Test */
            sut.onLogPollSuccess(Device.dummy(), MetaData.dummy().appPackage)

            /* Verify — no further events */
            expectNoEvents()
        }
    }

    @Test
    fun `onLogPollSuccess - device was disconnected - re-fetches metadata and reconnects`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())
        sut.onLogPollFailure(Device.dummy())  // disconnect first

        sut.selectedDevice.test {
            awaitItem()  // consume disconnected state

            /* Run Test */
            sut.onLogPollSuccess(Device.dummy(), MetaData.dummy().appPackage)

            /* Verify */
            assertTrue(awaitItem()?.isConnected == true)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onLogPollFailure - device connected - marks disconnected and notifies`() = runBlockingTest {
        /* Setup */
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

        sut.selectedDevice.test {
            awaitItem()  // consume initial emission

            /* Run Test */
            sut.onLogPollFailure(Device.dummy())

            /* Verify */
            assertFalse(awaitItem()?.isConnected ?: true)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onLogPollFailure - device already disconnected - no-op`() = runBlockingTest {
        /* Setup */
        val sut = createSut()
        sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())
        sut.onLogPollFailure(Device.dummy())  // disconnect once

        sut.selectedDevice.test {
            awaitItem()  // consume disconnected state

            /* Run Test */
            sut.onLogPollFailure(Device.dummy())

            /* Verify — no further events */
            expectNoEvents()
        }
    }
}
