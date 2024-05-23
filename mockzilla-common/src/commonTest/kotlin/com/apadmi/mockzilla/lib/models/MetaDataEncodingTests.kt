package com.apadmi.mockzilla.lib.models

import com.apadmi.mockzilla.lib.models.MetaData.Companion.parseMetaData
import kotlin.test.Test
import kotlin.test.assertEquals

class MetaDataEncodingTests {

    @Test
    fun `encode and decode - works correctly`() {
        /* Setup */
        val dummy = MetaData(
            appName = "Brady Hawkins",
            appPackage = "duis",
            operatingSystemVersion = "curabitur",
            deviceModel = "scelerisque",
            appVersion = "quaestio",
            runTarget = RunTarget.iOSSimulator,
            mockzillaVersion = "convenire"
        )

        /* Run Test */
        val thereAndBackAgain = dummy.toMap().parseMetaData()

        /* Verify */
        assertEquals(dummy, thereAndBackAgain)
    }
}