// Adapted from: https://github.com/ViToni/JmDNS-examples/blob/master/jmdns-examples/src/main/java/org/kromo/examples/network/NetworkUtils.java
package com.apadmi.mockzilla.desktop.engine.connection

import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.util.Enumeration

private const val googleDnsPort = 53
private val googleDns = InetSocketAddress("8.8.8.8", googleDnsPort)

fun Enumeration<NetworkInterface>.isLocalIpAddress(
    address: String
) = toList().any { networkInterface ->
    networkInterface.inetAddresses.toList().any { it.hostAddress == address }
}

fun Enumeration<NetworkInterface>.findMdnsAddresses() = asSequence()
    .filter { networkInterface ->
        networkInterface.isUp && networkInterface.supportsMulticast()
    }
    .map { networkInterface ->
        networkInterface.inetAddresses.toList()
            .filter { address ->
                true
                !address.isAnyLocalAddress &&
                !address.isLoopbackAddress &&
                runCatching {
                    DatagramSocket(0, address).use { it.connect(googleDns) }
                }.isSuccess
            }
    }
    .flatten()
    .toList()
