package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.internal.utils.SocketBinder
import io.ktor.network.sockets.ServerSocket

internal actual class MockSocketBinder actual constructor(private val whenBind: () -> ServerSocket) :
    SocketBinder {
    override suspend fun bind(hostname: String, port: Int): ServerSocket = whenBind()
}
