package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.connection.DetectedDevice
import com.apadmi.mockzilla.ui.engine.connection.IpAddress

import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
class DeviceDetectionUseCaseTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var adbConnectorServiceMock: AdbConnectorService

    @Test
    fun `onChangedServiceEvent - various cases - are correct`() = runBlockingTest {
        listOf(
            ChangedServiceEventTestCase(
                caseDescription = "Android Device - Resolving - But with metadata",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("a"),
                    MetaData.dummy().copy(runTarget = RunTarget.AndroidDevice).toMap(),
                    8080,
                    DeviceDiscoveryEvent.State.Found
                ),
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("a")),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidDevice),
                    port = 8080,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "Android Device - Resolving",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("a"),
                    mapOf(),
                    8080,
                    DeviceDiscoveryEvent.State.Found
                ),
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("a")),
                    metaData = null,
                    port = 8080,
                    adbConnection = null,
                    prettyName = "connection name",
                    state = DetectedDevice.State.Resolving
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Device - Resolving",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b"),
                    mapOf(),
                    8_087_854,
                    DeviceDiscoveryEvent.State.Found
                ),
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("b")),
                    metaData = null,
                    port = 8_087_854,
                    adbConnection = null,
                    prettyName = "connection name",
                    state = DetectedDevice.State.Resolving
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Device - Resolved",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
                    8_087_854,
                    DeviceDiscoveryEvent.State.Resolved
                ),
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(IpAddress("b")),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 8_087_854,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Simulator - Is local sim",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b", "my local machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosSimulator).toMap(),
                    8_087_854,
                    DeviceDiscoveryEvent.State.Resolved
                ),
                isLocalIpAddress = true,
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(
                        "b",
                        "my local machine ip address"
                    ).map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosSimulator),
                    port = 8_087_854,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "iOS Simulator - Is someone else's sim",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("b", "some remote machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.IosSimulator).toMap(),
                    1_111_111,
                    DeviceDiscoveryEvent.State.Resolved
                ),
                isLocalIpAddress = false,
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf(
                        "b",
                        "some remote machine ip address"
                    ).map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosSimulator),
                    port = 1_111_111,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.NotYourSimulator
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "Android Emulator - Is local emulator",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some local machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator).toMap(),
                    13_111_111,
                    DeviceDiscoveryEvent.State.Resolved
                ),
                mockAdbConnection = AdbConnection(
                    deviceSerial = "serial",
                    isActive = true,
                    ipAddresses = listOf(IpAddress("some local machine ip address"))
                ),
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some local machine ip address").map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator),
                    port = 13_111_111,
                    adbConnection = AdbConnection(
                        "serial",
                        true,
                        listOf(IpAddress("some local machine ip address"))
                    ),
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            ChangedServiceEventTestCase(
                caseDescription = "Android Emulator - Is someone else's emulator",
                info = DeviceDiscoveryEvent(
                    connectionName = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some remote machine ip address"),
                    MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator).toMap(),
                    13_111_111,
                    DeviceDiscoveryEvent.State.Resolved
                ),
                mockAdbConnection = AdbConnection(
                    deviceSerial = "serial",
                    isActive = true,
                    ipAddresses = listOf(IpAddress("some local machine ip address"))
                ),
                expectedResult = DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = listOf("some remote machine ip address").map { IpAddress(it) },
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator),
                    port = 13_111_111,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.NotYourSimulator
                )
            )
        ).forEach { testCase ->
            /* Setup */
            coEvery { adbConnectorServiceMock.listConnectedDevices() }
                .returns(Result.success(listOfNotNull(testCase.mockAdbConnection)))

            val sut = DeviceDetectionUseCaseImpl({ testCase.isLocalIpAddress },
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
        coEvery { adbConnectorServiceMock.listConnectedDevices() }
            .returns(Result.success(emptyList()))

        val dummy = DeviceDiscoveryEvent(
            connectionName = "connection name",
            hostAddress = "host",
            hostAddresses = listOf(),
            MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
            13_111_111,
            DeviceDiscoveryEvent.State.Resolved
        )
        val sut = DeviceDetectionUseCaseImpl({ true }, adbConnectorServiceMock)

        /* Run Test */
        sut.onChangedServiceEvent(dummy)
        val result1 = sut.devices
        sut.onChangedServiceEvent(dummy.copy(state = DeviceDiscoveryEvent.State.Removed))
        val result2 = sut.devices

        /* Verify */
        assertEquals(
            expected = listOf(
                DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = emptyList(),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 13_111_111,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.ReadyToConnect
                )
            ),
            actual = result1
        )
        assertEquals(
            expected = listOf(
                DetectedDevice(
                    connectionId = "connection name",
                    hostAddress = "host",
                    hostAddresses = emptyList(),
                    metaData = MetaData.dummy().copy(runTarget = RunTarget.IosDevice),
                    port = 13_111_111,
                    adbConnection = null,
                    prettyName = "Jannie Bates (erat)",
                    state = DetectedDevice.State.Removed
                )
            ),
            actual = result2
        )
    }

    @Test
    fun `onChangedServiceEvent Resolved to Found - update ignored`() = runBlockingTest {
        /* Setup */
        coEvery { adbConnectorServiceMock.listConnectedDevices() }
            .returns(Result.success(emptyList()))

        val dummy = DeviceDiscoveryEvent(
            connectionName = "connection name",
            hostAddress = "host",
            hostAddresses = listOf(),
            MetaData.dummy().copy(runTarget = RunTarget.IosDevice).toMap(),
            13_111_111,
            DeviceDiscoveryEvent.State.Resolved
        )
        val sut = DeviceDetectionUseCaseImpl({ true }, adbConnectorServiceMock)

        /* Run Test */
        sut.onChangedServiceEvent(dummy)
        val result1 = sut.devices
        sut.onChangedServiceEvent(dummy.copy(state = DeviceDiscoveryEvent.State.Found))
        val result2 = sut.devices

        /* Verify */
        assertEquals(
            expected = result1,
            actual = result2
        )
    }

    @Test
    fun `onChangedServiceEvent - ADB path - discovered emulator - creates ReadyToConnect device at loopback`() = runBlockingTest {
        val adbConnection = AdbConnection("emulator-5554", true, emptyList())
        val metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator)
        val sut = DeviceDetectionUseCaseImpl({ false }, adbConnectorServiceMock)

        sut.onChangedServiceEvent(DeviceDiscoveryEvent(
            connectionName = "adb:emulator-5554:8080",
            hostAddress = "127.0.0.1",
            hostAddresses = listOf("127.0.0.1"),
            attributes = emptyMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved,
            adbConnection = adbConnection,
            metaData = metaData
        ))

        assertEquals(
            expected = listOf(DetectedDevice(
                connectionName = "adb:emulator-5554:8080",
                metaData = metaData,
                hostAddress = "127.0.0.1",
                hostAddresses = listOf(IpAddress("127.0.0.1")),
                port = 8080,
                adbConnection = adbConnection,
                state = DetectedDevice.State.ReadyToConnect
            )),
            actual = sut.devices
        )
    }

    @Test
    fun `onChangedServiceEvent - ADB path - same event twice - no duplicate`() = runBlockingTest {
        val adbConnection = AdbConnection("emulator-5554", true, emptyList())
        val metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator)
        val sut = DeviceDetectionUseCaseImpl({ false }, adbConnectorServiceMock)
        val event = DeviceDiscoveryEvent(
            connectionName = "adb:emulator-5554:8080",
            hostAddress = "127.0.0.1",
            hostAddresses = listOf("127.0.0.1"),
            attributes = emptyMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved,
            adbConnection = adbConnection,
            metaData = metaData
        )

        sut.onChangedServiceEvent(event)
        sut.onChangedServiceEvent(event)

        assertEquals(1, sut.devices.size)
    }

    @Test
    fun `onChangedServiceEvent - ADB path - replaces mDNS entry for same serial and port`() = runBlockingTest {
        val adbConnection = AdbConnection("emulator-5554", true, listOf(IpAddress("10.0.2.15")))
        val metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator)
        coEvery { adbConnectorServiceMock.listConnectedDevices() }
            .returns(Result.success(listOf(adbConnection)))
        val sut = DeviceDetectionUseCaseImpl({ false }, adbConnectorServiceMock)

        sut.onChangedServiceEvent(DeviceDiscoveryEvent(
            connectionName = "mdns-service-name",
            hostAddress = "10.0.2.15",
            hostAddresses = listOf("10.0.2.15"),
            attributes = metaData.toMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved
        ))

        sut.onChangedServiceEvent(DeviceDiscoveryEvent(
            connectionName = "adb:emulator-5554:8080",
            hostAddress = "127.0.0.1",
            hostAddresses = listOf("127.0.0.1"),
            attributes = emptyMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved,
            adbConnection = adbConnection,
            metaData = metaData
        ))

        // ADB evicts the mDNS entry and takes over
        assertEquals(1, sut.devices.size)
        assertEquals("adb:emulator-5554:8080", sut.devices.single().connectionName)
        assertEquals("127.0.0.1", sut.devices.single().hostAddress)
    }

    @Test
    fun `onChangedServiceEvent - ADB path - emulator lost - marks Removed`() = runBlockingTest {
        val adbConnection = AdbConnection("emulator-5554", true, emptyList())
        val metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator)
        val sut = DeviceDetectionUseCaseImpl({ false }, adbConnectorServiceMock)

        val discoveredEvent = DeviceDiscoveryEvent(
            connectionName = "adb:emulator-5554:8080",
            hostAddress = "127.0.0.1",
            hostAddresses = listOf("127.0.0.1"),
            attributes = emptyMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved,
            adbConnection = adbConnection,
            metaData = metaData
        )
        sut.onChangedServiceEvent(discoveredEvent)
        sut.onChangedServiceEvent(discoveredEvent.copy(
            state = DeviceDiscoveryEvent.State.Removed,
            adbConnection = AdbConnection("emulator-5554", false, emptyList())
        ))

        assertEquals(
            expected = listOf(DetectedDevice(
                connectionName = "adb:emulator-5554:8080",
                metaData = metaData,
                hostAddress = "127.0.0.1",
                hostAddresses = listOf(IpAddress("127.0.0.1")),
                port = 8080,
                adbConnection = adbConnection,
                state = DetectedDevice.State.Removed
            )),
            actual = sut.devices
        )
    }

    @Test
    fun `onChangedServiceEvent - ADB path - lost emulator not in cache - no change`() = runBlockingTest {
        val sut = DeviceDetectionUseCaseImpl({ false }, adbConnectorServiceMock)

        sut.onChangedServiceEvent(DeviceDiscoveryEvent(
            connectionName = "adb:emulator-5554:8080",
            hostAddress = "127.0.0.1",
            hostAddresses = emptyList(),
            attributes = emptyMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Removed,
            adbConnection = AdbConnection("emulator-5554", false, emptyList())
        ))

        assertEquals(expected = emptyList(), actual = sut.devices)
    }

    @Test
    fun `onChangedServiceEvent - ZeroConf path - mDNS ignored if ADB already verified same serial and port`() = runBlockingTest {
        val adbConnection = AdbConnection("emulator-5554", true, listOf(IpAddress("10.0.2.15")))
        val metaData = MetaData.dummy().copy(runTarget = RunTarget.AndroidEmulator)
        coEvery { adbConnectorServiceMock.listConnectedDevices() }
            .returns(Result.success(listOf(adbConnection)))
        val sut = DeviceDetectionUseCaseImpl({ false }, adbConnectorServiceMock)

        sut.onChangedServiceEvent(DeviceDiscoveryEvent(
            connectionName = "adb:emulator-5554:8080",
            hostAddress = "127.0.0.1",
            hostAddresses = listOf("127.0.0.1"),
            attributes = emptyMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved,
            adbConnection = adbConnection,
            metaData = metaData
        ))
        val afterAdb = sut.devices

        sut.onChangedServiceEvent(DeviceDiscoveryEvent(
            connectionName = "mdns-service-name",
            hostAddress = "10.0.2.15",
            hostAddresses = listOf("10.0.2.15"),
            attributes = metaData.toMap(),
            port = 8080,
            state = DeviceDiscoveryEvent.State.Resolved
        ))

        // mDNS event discarded — ADB entry unchanged
        assertEquals(expected = afterAdb, actual = sut.devices)
        assertEquals(1, sut.devices.size)
        assertEquals("adb:emulator-5554:8080", sut.devices.single().connectionName)
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
     * @property isLocalIpAddress
     * @property expectedResult
     */
    data class ChangedServiceEventTestCase(
        val caseDescription: String,
        val info: DeviceDiscoveryEvent,
        val mockAdbConnection: AdbConnection? = null,
        val isLocalIpAddress: Boolean = true,
        val expectedResult: DetectedDevice
    )
}
