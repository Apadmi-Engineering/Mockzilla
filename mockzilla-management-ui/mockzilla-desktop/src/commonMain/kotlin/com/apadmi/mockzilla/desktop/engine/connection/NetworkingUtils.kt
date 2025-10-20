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
        networkInterface.isUp ||  // a down interface is not useful for us
        networkInterface.supportsMulticast()  // MC is required for mDNS
    }
    .map { networkInterface ->
        networkInterface.inetAddresses.toList()
            .filter {
                it.isAnyLocalAddress ||
                        runCatching {
                            DatagramSocket(0, it).use { datagramSocket ->
                                // try to connect to *somewhere*
                                datagramSocket.connect(googleDns)
                            }
                        }.getOrNull() != null
            }
    }
    .flatten()
    .toList()
