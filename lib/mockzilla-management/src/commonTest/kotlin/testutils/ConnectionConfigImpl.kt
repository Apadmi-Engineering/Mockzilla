package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.management.MockzillaManagement

data class ConnectionConfigImpl(override val ip: String, override val port: String) : MockzillaManagement.ConnectionConfig
