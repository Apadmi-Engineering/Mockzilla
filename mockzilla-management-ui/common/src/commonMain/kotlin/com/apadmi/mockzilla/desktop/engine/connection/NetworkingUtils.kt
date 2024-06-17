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

suspend fun Enumeration<NetworkInterface>.findMdnsAddresses() = toList()
    .filterNot { networkInterface ->
        !networkInterface.isUp ||  // a down interface is not useful for us, isn't it
        !networkInterface.supportsMulticast() ||  // MC is required for mDNS
        networkInterface.isPointToPoint ||  // we care only about regular interfaces
        networkInterface.isLoopback  // don't care about loopback addresses
    }.firstNotNullOfOrNull { networkInterface ->
        networkInterface.inetAddresses.toList()
            .filterNot { it.isLinkLocalAddress }
            .filter {
                runCatching {
                    DatagramSocket(0, it).use { datagramSocket ->
                        // try to connect to *somewhere*
                        datagramSocket.connect(googleDns)
                    }
                }.getOrNull() != null
            }
    } ?: emptyList()
