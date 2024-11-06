package com.apadmi.mockzilla.lib.internal.utils

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket

internal class SocketIo(private val selectorManager: SelectorManager) {
    suspend fun isPortAvailable(port: Int): Boolean = runCatching {
        val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", port)
        serverSocket.dispose()
    }.isSuccess
}
