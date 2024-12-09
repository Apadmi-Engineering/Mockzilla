package com.apadmi.mockzilla.desktop.utils

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@Suppress("MAGIC_NUMBER")
class DataWithTimestampTests {
    @Test
    fun `isExpired - expired - returns true`() {
        val initialTimeStamp = System.currentTimeMillis()
        val timesStampAtExpiryCheck = System.currentTimeMillis() + 10.seconds.inWholeMilliseconds
        assertTrue(
            DataWithTimestamp(
                "",
                initialTimeStamp
            ).isExpired(9.seconds, timesStampAtExpiryCheck)
        )
    }

    @Test
    fun `isExpired - not expired - returns false`() {
        val initialTimeStamp = System.currentTimeMillis()
        val timesStampAtExpiryCheck = System.currentTimeMillis() + 8.seconds.inWholeMilliseconds
        assertFalse(
            DataWithTimestamp(
                "",
                initialTimeStamp
            ).isExpired(9.seconds, currentTimeStamp = timesStampAtExpiryCheck)
        )
    }
}
