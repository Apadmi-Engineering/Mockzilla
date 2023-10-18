package com.apadmi.mockzilla.desktop.utils

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class DataWithTimestampTests {
    @Test
    fun `isExpired - expired - returns true`() {
        assertTrue(
            DataWithTimestamp(
                "",
                System.currentTimeMillis() - 10.seconds.inWholeMilliseconds
            ).isExpired(9.seconds)
        )
    }

    @Test
    fun `isExpired - not expired - returns false`() {
        assertTrue(
            DataWithTimestamp(
                "",
                System.currentTimeMillis() - 8.seconds.inWholeMilliseconds
            ).isExpired(9.seconds)
        )
    }
}