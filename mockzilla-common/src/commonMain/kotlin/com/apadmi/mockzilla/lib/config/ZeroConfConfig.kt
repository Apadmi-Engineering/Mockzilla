package com.apadmi.mockzilla.lib.config

/**
 * Constants for Mockzilla's ZeroConf (Bonjour/DNS-SD) service discovery integration. Used by
 * the server to advertise itself and by the management UI to locate devices on the network.
 */
public object ZeroConfConfig {
    /**
     * The ZeroConf service type Mockzilla registers under.
     */
    public const val serviceType: String = "_mockzilla._tcp"

    /**
     * Maximum byte length for a ZeroConf service name, as defined by RFC 1035 section 2.3.1.
     */
    public const val serviceNameByteLimit: Int = 63
}
