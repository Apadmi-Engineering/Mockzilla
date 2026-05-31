// Adapted from: https://github.com/ViToni/JmDNS-examples/blob/master/jmdns-examples/src/main/java/org/kromo/examples/network/NetworkUtils.java
package com.apadmi.mockzilla.desktop.engine.connection

import java.net.NetworkInterface
import java.util.Enumeration

fun Enumeration<NetworkInterface>.isLocalIpAddress(
    address: String
) = toList().any { networkInterface ->
    networkInterface.inetAddresses.toList().any { it.hostAddress == address }
}

fun Enumeration<NetworkInterface>.findMdnsAddresses() = asSequence()
    .map { networkInterface ->
        networkInterface.inetAddresses.toList()
    }
    .flatten()
    .toList()
