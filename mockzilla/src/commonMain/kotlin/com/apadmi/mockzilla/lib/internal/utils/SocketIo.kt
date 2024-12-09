package com.apadmi.mockzilla.lib.internal.utils

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.aSocket

internal interface SocketBinder {
    suspend fun bind(hostname: String, port: Int): ServerSocket
}

internal class SocketBinderImpl(private val selectorManager: SelectorManager) : SocketBinder {
    override suspend fun bind(hostname: String, port: Int): ServerSocket =
        aSocket(selectorManager).tcp().bind(hostname, port)
}

internal class SocketIo(private val socketBinder: SocketBinder) {

    /**
     * Ktor treats port `0` as a flag to use a random, available port. See more at
     * https://ktor.io/docs/server-configuration-code.html#embedded-basic
     */
    private val Int.isRandomPortFlag: Boolean
        get() = this == 0

    suspend fun isPortAvailable(port: Int): Boolean = port.isRandomPortFlag || runCatching {
        val serverSocket = socketBinder.bind("127.0.0.1", port)
        serverSocket.dispose()
    }.isSuccess
}
