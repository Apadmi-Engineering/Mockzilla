package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.management.MockzillaConnectionConfig

data class MockzillaConnectionConfigImpl(
    override val ip: String,
    override val port: String
) : MockzillaConnectionConfig
