package com.apadmi.mockzilla.management

/**
 * Defines the info needed to create a connection to a device. (i.e. make a request)
 */
interface MockzillaConnectionConfig {
    val ip: String
    val port: String
}
