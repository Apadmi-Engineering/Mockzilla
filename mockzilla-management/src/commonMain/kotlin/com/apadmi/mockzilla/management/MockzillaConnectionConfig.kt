package com.apadmi.mockzilla.management

/**
 * Defines the connection details needed to target a specific device running a Mockzilla server.
 */
interface MockzillaConnectionConfig {
    /**
     * The IP address of the device.
     */
    val ip: String

    /**
     * The port the Mockzilla server is bound to on the device.
     */
    val port: String
}
