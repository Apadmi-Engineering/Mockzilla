package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.testutils.MockSelectorManager

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
            val mockSelectorManager = MockSelectorManager(this.coroutineContext)
            val sut = SocketIo(mockSelectorManager)

            /* Run test & verify */

            val actual = sut.isPortAvailable(portFixture)

            /* Verify */
            assertEquals(true, actual)
        }
    }

    @Test
    fun `isPortAvailable - upon bind failure - answers false`() = runTest {
        runBlocking {
            /* Setup */
            val mockSelectorManager =
                MockSelectorManager(this.coroutineContext) {
                    throw IOException()
                }
            val sut = SocketIo(mockSelectorManager)

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
