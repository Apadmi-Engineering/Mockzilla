package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement

data class MockzillaConnectionConfigImpl(
    override val ip: String,
    override val port: String
) : MockzillaConnectionConfig
