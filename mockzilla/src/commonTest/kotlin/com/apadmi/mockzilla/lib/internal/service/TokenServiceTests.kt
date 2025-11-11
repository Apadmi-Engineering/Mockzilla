package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.testutils.fakes.FakeClock

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class TokenServiceTests {
    @OptIn(ExperimentalTime::class)
    @Test
    fun `getValidToken - gives new token`() = runTest {
        /* Setup */
        val sut = TokensServiceImpl(1.seconds)

        /* Run Test */
        val token1 = sut.getValidToken()
        val token2 = sut.getValidToken()

        /* Verify */
        assertTrue(token1.trim().length > 5)
        assertTrue(token2.trim().length > 5)
        assertNotEquals(token1, token2)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `isTokenValid - isValid - returns true`() = runTest {
        /* Setup */
        val sut = TokensServiceImpl(1.seconds)

        /* Run Test */
        val isValid = sut.isTokenValid(sut.getValidToken())

        /* Verify */
        assertTrue(isValid)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `isTokenValid - used twice - returns false`() = runTest {
        /* Setup */
        val sut = TokensServiceImpl(1.seconds)
        val token = sut.getValidToken()
        sut.isTokenValid(token)  // run once

        /* Run Test */
        val isValid = sut.isTokenValid(token)

        /* Verify */
        assertFalse(isValid)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `isTokenValid - expired - returns false`() = runTest {
        /* Setup */
        val fakeClock = FakeClock()
        val sut = TokensServiceImpl(0.5.seconds, fakeClock)
        val token = sut.getValidToken()

        /* Run Test */
        fakeClock.now = fakeClock.now.plus(6.seconds)
        val isValid = sut.isTokenValid(token)

        /* Verify */
        assertFalse(isValid)
    }
}
