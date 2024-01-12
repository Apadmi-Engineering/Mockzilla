package com.apadmi.mockzilla.lib.internal.service

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Suppress("MAGIC_NUMBER")
class TokenServiceTests {
    @Test
    fun `getValidToken - gives new token`() = runBlocking {
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

    @Test
    fun `isTokenValid - isValid - returns true`() = runBlocking {
        /* Setup */
        val sut = TokensServiceImpl(1.seconds)

        /* Run Test */
        val isValid = sut.isTokenValid(sut.getValidToken())

        /* Verify */
        assertTrue(isValid)
    }

    @Test
    fun `isTokenValid - used twice - returns false`() = runBlocking {
        /* Setup */
        val sut = TokensServiceImpl(1.seconds)
        val token = sut.getValidToken()
        sut.isTokenValid(token)  // run once

        /* Run Test */
        val isValid = sut.isTokenValid(token)

        /* Verify */
        assertFalse(isValid)
    }

    @Test
    fun `isTokenValid - expired - returns false`() = runBlocking {
        /* Setup */
        val sut = TokensServiceImpl(0.5.seconds)
        val token = sut.getValidToken()

        /* Run Test */
        delay(600)
        val isValid = sut.isTokenValid(token)

        /* Verify */
        assertFalse(isValid)
    }
}
