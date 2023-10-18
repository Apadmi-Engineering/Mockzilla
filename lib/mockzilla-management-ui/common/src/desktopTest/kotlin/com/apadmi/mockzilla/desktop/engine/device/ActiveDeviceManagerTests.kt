package  com.apadmi.mockzilla.desktop.engine.device

import app.cash.turbine.test
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ActiveDeviceManagerTests {

    @Mock
    private val metaDataUseCaseMock = mock(classOf<MetaDataUseCase>())

    private fun createSut() = ActiveDeviceManagerImpl(
        metaDataUseCaseMock,
        CoroutineScope(Dispatchers.Default)
    )

    @Test
    fun `updateActiveDevice - updates device and notifies listeners`() = runTest {
        /* Setup */
        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.updateActiveDevice(Device.dummy())

            /* Verify */
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `setActiveDeviceWithMetaData - updates device and notifies listeners`() = runTest {
        /* Setup */
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
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `clearActiveDevice - clears notifies listeners`() = runTest {
        /* Setup */
        val sut = createSut()

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.clearActiveDevice()

            /* Verify */
            assertNull(sut.activeDevice)
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `monitorDeviceConnections - app package changes - notifies device change listeners`() = runTest {
        val sut = createSut()

        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy().copy(appPackage = "new.package")))

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Verify */
            assertEquals(Unit, awaitItem())
            assertEquals(Unit, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `monitorDeviceConnections - app package the same - does not notify device change listeners`() = runTest {
        val sut = createSut()

        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy()))

        sut.onDeviceSelectionChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Verify */
            skipItems(1) // One event for setting active device
            expectNoEvents()
        }
    }

    @Test
    fun `monitorDeviceConnections - fails to get metadata - updates connection status`() = runTest {
        val sut = createSut()

        given(metaDataUseCaseMock).coroutine {
            getMetaData(Device.dummy())
        }.thenReturn(Result.failure(Exception()))

        sut.onDeviceConnectionStateChange.test {
            /* Run Test */
            sut.setActiveDeviceWithMetaData(Device.dummy(), MetaData.dummy())

            /* Verify */
            assertEquals(Unit, awaitItem())
            assertFalse(sut.allDevices.first().isConnected)
            ensureAllEventsConsumed()
        }
    }
}
