package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.config.ZeroConfConfig.Companion.parseZeroConfConfig
import com.apadmi.mockzilla.lib.models.RunTarget
import kotlin.test.Test
import kotlin.test.assertEquals

class ZeroConfConfigTests {

    @Test
    fun `encode and decode - works correctly`() {
        /* Setup */
        val dummy = ZeroConfConfig(
            isEmulator = true,
            clientPackage = "my package",
            os = RunTarget.Android
        )

        /* Run Test */
        val thereAndBackAgain = dummy.toMap().parseZeroConfConfig()

        /* Verify */
        assertEquals(dummy, thereAndBackAgain)
    }
}