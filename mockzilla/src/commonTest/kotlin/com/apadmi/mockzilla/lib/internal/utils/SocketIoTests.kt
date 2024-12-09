package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.testutils.MockSocketBinder
import com.apadmi.mockzilla.testutils.fakes.FakeServerSocket

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException

internal class SocketIoTests {
    @Test
    fun `isPortAvailable - upon bind success - answers true`() = runTest {
        runBlocking {
            /* Setup */
            val mockSocketBinder = MockSocketBinder {
                FakeServerSocket()
            }
            val sut = SocketIo(mockSocketBinder)

            /* Run test & verify */

            val actual = sut.isPortAvailable(portFixture)

            /* Verify */
            assertEquals(true, actual)
        }
    }

    @Test
    fun `isPortAvailable - with random port flag - answers true`() = runTest {
        runBlocking {
            /* Setup */
            val mockSocketBinder = MockSocketBinder {
                throw IOException()
            }
            val sut = SocketIo(mockSocketBinder)

            /* Run test & verify */

            val actual = sut.isPortAvailable(0)

            /* Verify */
            assertEquals(true, actual)
        }
    }

    @Test
    fun `isPortAvailable - upon bind failure - answers false`() = runTest {
        runBlocking {
            /* Setup */
            val mockSocketBinder = MockSocketBinder {
                throw IOException()
            }
            val sut = SocketIo(mockSocketBinder)

            /* Run test & verify */
            val actual = sut.isPortAvailable(portFixture)

            /* Verify */
            assertEquals(false, actual)
        }
    }

    companion object {
        private val portFixture = 8080
    }
}
