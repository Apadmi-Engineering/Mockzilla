package com.apadmi.mockzilla.lib.internal.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("MAGIC_NUMBER")
class DelayAndFailureDecisionTests {
    @Test
    fun `shouldFail - 0 probability - returns false`() {
        /* Setup */
        val sut = DelayAndFailureDecisionImpl

        /* Run Test */
        val result = sut.shouldFail(0)

        /* Verify */
        assertFalse(result)
    }

    @Test
    fun `shouldFail - 100 probability - returns false`() {
        /* Setup */
        val sut = DelayAndFailureDecisionImpl

        /* Run Test */
        val result = sut.shouldFail(100)

        /* Verify */
        assertTrue(result)
    }

    @Test
    fun `generateDelayMillis - mean=variance=0 - returns 0`() {
        /* Setup */
        val sut = DelayAndFailureDecisionImpl

        /* Run Test */
        val result = sut.generateDelayMillis(0, 0)

        /* Verify */
        assertEquals(0, result)
    }

    @Test
    fun `generateDelayMillis - variance=0 - returns mean`() {
        /* Setup */
        val sut = DelayAndFailureDecisionImpl

        /* Run Test */
        val result = sut.generateDelayMillis(200, 0)

        /* Verify */
        assertEquals(200, result)
    }
}
