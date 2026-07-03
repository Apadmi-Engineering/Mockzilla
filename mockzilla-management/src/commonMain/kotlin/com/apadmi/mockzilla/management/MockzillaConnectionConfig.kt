package com.apadmi.mockzilla.management

/**
 * Defines the connection details needed to target a specific device running a Mockzilla server.
 */
public interface MockzillaConnectionConfig {
    /**
     * The IP address of the device.
     */
    public val ip: String

    /**
     * The port the Mockzilla server is bound to on the device.
     */
    public val port: String
}
