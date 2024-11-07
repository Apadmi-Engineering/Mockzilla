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
    suspend fun isPortAvailable(port: Int): Boolean = runCatching {
        val serverSocket = socketBinder.bind("127.0.0.1", port)
        serverSocket.dispose()
    }.isSuccess
}
