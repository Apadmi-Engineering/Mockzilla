package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.engine.connection.jmdns.ServiceInfoWrapper
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
class DeviceDetectionUseCaseTests : CoroutineTest() {
    @Mock
    private val adbConnectorServiceMock = mock(classOf<AdbConnectorService>())

    @Test
    fun `onChangedServiceEvent - various cases - are correct`() = runBlockingTest {
        listOf(
            ChangedServiceEventTestCase(
                caseDescription = "Android Device - Resolving",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("a"),
                    MetaData.dummy().copy(runTarget = RunTarget.AndroidDevice).toMap(),
                    8080,
                    ServiceInfoWrapper.State.Found
                ),
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("a")),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidDevice),
                    port = 8080,
                    adbConnection = null,
                    state = DetectedDevice.State.Resolving
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Device - Resolving",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
                    8_087_854,
                    ServiceInfoWrapper.State.Found
                ),
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("b")),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 8_087_854,
                    adbConnection = null,
                    state = DetectedDevice.State.Resolving
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Device - Resolved",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
                    8_087_854,
                    ServiceInfoWrapper.State.Resolved
                ),
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("b")),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 8_087_854,
                    adbConnection = null,
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Simulator - Is local sim",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b", "my local machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosSimulator).toMap(),
                    8_087_854,
                    ServiceInfoWrapper.State.Resolved
                ),
                localIpAddress = "my local machine ip address",
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(
                        "b",
                        "my local machine ip address"
                    ).map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosSimulator),
                    port = 8_087_854,
                    adbConnection = null,
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Simulator - Is someone else's sim",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b", "some remote machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosSimulator).toMap(),
                    1_111_111,
                    ServiceInfoWrapper.State.Resolved
                ),
                localIpAddress = "my local machine ip address",
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(
                        "b",
                        "some remote machine ip address"
                    ).map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosSimulator),
                    port = 1_111_111,
                    adbConnection = null,
                    state = DetectedDevice.State.NotYourSimulator
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "Android Emulator - Is local emulator",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some local machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator).toMap(),
                    13_111_111,
                    ServiceInfoWrapper.State.Resolved
                ),
                mockAdbConnection = AdbConnection(
                    deviceSerial = "serial",
                    isActive = true,
                    ipAddresses = listOf(IpAddress("some local machine ip address"))
                ),
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some local machine ip address").map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator),
                    port = 13_111_111,
                    adbConnection = AdbConnection(
                        "serial",
                        true,
                        listOf(IpAddress("some local machine ip address"))
                    ),
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "Android Emulator - Is someone else's emulator",
                info = ServiceInfoWrapper(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some remote machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator).toMap(),
                    13_111_111,
                    ServiceInfoWrapper.State.Resolved
                ),
                mockAdbConnection = AdbConnection(
                    deviceSerial = "serial",
                    isActive = true,
                    ipAddresses = listOf(IpAddress("some local machine ip address"))
                ),
                expectedResult = DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some remote machine ip address").map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator),
                    port = 13_111_111,
                    adbConnection = null,
                    state = DetectedDevice.State.NotYourSimulator
                )
            )
        ).forEach { testCase ->
            /* Setup */
            given(adbConnectorServiceMock)
                .coroutine { listConnectedDevices() }
                .thenReturn(Result.success(listOfNotNull(testCase.mockAdbConnection)))

            val sut = DeviceDetectionUseCaseImpl({ testCase.localIpAddress },
                adbConnectorServiceMock
            )

            /* Run Test */
            sut.onChangedServiceEvent(testCase.info)

            /* Verify */
            assertEquals(
                message = testCase.caseDescription,
                expected = listOf(testCase.expectedResult),
                actual = sut.devices
            )
        }
    }

    @Test
    fun `onChangedServiceEvent Resolved to Removed - updates correctly`() = runBlockingTest {
        /* Setup */
        given(adbConnectorServiceMock)
            .coroutine { listConnectedDevices() }
            .thenReturn(Result.success(emptyList()))

        val dummy = ServiceInfoWrapper(
            connectionName = "connection name",
            hostAddress = "host",
            hostAddresses = listOf(),
            MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
            13_111_111,
            ServiceInfoWrapper.State.Resolved
        )
        val sut = DeviceDetectionUseCaseImpl({ "" }, adbConnectorServiceMock)

        /* Run Test */
        sut.onChangedServiceEvent(dummy)
        val result1 = sut.devices
        sut.onChangedServiceEvent(dummy.copy(state = ServiceInfoWrapper.State.Removed))
        val result2 = sut.devices

        /* Verify */
        assertEquals(
            expected = listOf(
                DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = emptyList(),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 13_111_111,
                    adbConnection = null,
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            actual = result1
        )
        assertEquals(
            expected = listOf(
                DetectedDevice(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = emptyList(),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 13_111_111,
                    adbConnection = null,
                    state = DetectedDevice.State.Removed
                )
            ),
            actual = result2
        )
    }

    @Test
    fun `onChangedServiceEvent Resolved to Found - update ignored`() = runBlockingTest {
        /* Setup */
        given(adbConnectorServiceMock)
            .coroutine { listConnectedDevices() }
            .thenReturn(Result.success(emptyList()))

        val dummy = ServiceInfoWrapper(
            connectionName = "connection name",
            hostAddress = "host",
            hostAddresses = listOf(),
            MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
            13_111_111,
            ServiceInfoWrapper.State.Resolved
        )
        val sut = DeviceDetectionUseCaseImpl({ "" }, adbConnectorServiceMock)

        /* Run Test */
        sut.onChangedServiceEvent(dummy)
        val result1 = sut.devices
        sut.onChangedServiceEvent(dummy.copy(state = ServiceInfoWrapper.State.Found))
        val result2 = sut.devices

        /* Verify */
        assertEquals(
            expected = result1,
            actual = result2
        )
    }

    @Test
    fun `matchAdbDeviceFromHostAddresses - various cases - are correct`() {
        assertEquals(
            message = "Match",
            expected = AdbConnection.dummy(listOf("ipAddress")),
            actual = DeviceDetectionUseCaseImpl.matchAdbDeviceFromHostAddresses(
                listOf(AdbConnection.dummy(listOf("ipAddress"))),
                hostAddresses = setOf(IpAddress("ipAddress"))
            )
        )
        assertEquals(
            message = "Match with multiple",
            expected = AdbConnection.dummy(listOf("ipAddress")),
            actual = DeviceDetectionUseCaseImpl.matchAdbDeviceFromHostAddresses(
                listOf(
                    AdbConnection.dummy(listOf("ipAddress")),
                    AdbConnection.dummy(listOf("ipAddress2")),
                    AdbConnection.dummy(listOf("ipAddress3")),
                ),
                hostAddresses = setOf(IpAddress("ipAddress"))
            )
        )
        assertEquals(
            message = "Match with multiple host addresses",
            expected = AdbConnection.dummy(listOf("ipAddress")),
            actual = DeviceDetectionUseCaseImpl.matchAdbDeviceFromHostAddresses(
                listOf(
                    AdbConnection.dummy(listOf("ipAddress")),
                    AdbConnection.dummy(listOf("ipAddress2")),
                    AdbConnection.dummy(listOf("ipAddress3")),
                ),
                hostAddresses = setOf(
                    IpAddress("hello"),
                    IpAddress("world"),
                    IpAddress("ipAddress")
                )
            )
        )
        assertEquals(
            message = "No match",
            expected = null,
            actual = DeviceDetectionUseCaseImpl.matchAdbDeviceFromHostAddresses(
                listOf(
                    AdbConnection.dummy(listOf("ipAddress")),
                    AdbConnection.dummy(listOf("ipAddress2")),
                    AdbConnection.dummy(listOf("ipAddress3")),
                ),
                hostAddresses = setOf(
                    IpAddress("hello no match"),
                    IpAddress("world no match"),
                    IpAddress("ipAddress no match")
                )
            )
        )
    }

    /**
     * @property caseDescription
     * @property info
     * @property mockAdbConnection
     * @property localIpAddress
     * @property expectedResult
     */
    data class ChangedServiceEventTestCase(
        val caseDescription: String,
        val info: ServiceInfoWrapper,
        val mockAdbConnection: AdbConnection? = null,
        val localIpAddress: String = "",
        val expectedResult: DetectedDevice
    )
}
