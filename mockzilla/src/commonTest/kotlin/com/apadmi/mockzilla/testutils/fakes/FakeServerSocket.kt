package com.apadmi.mockzilla.testutils.fakes

import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.SocketAddress
import kotlinx.coroutines.Job

internal class FakeServerSocket : ServerSocket {
    override val localAddress: SocketAddress
        get() = throw NotImplementedError()
    override val socketContext: Job
        get() = throw NotImplementedError()
    override fun close() {
        /* Intentionally blank. */
    }

    override suspend fun accept(): Socket {
        throw NotImplementedError()
    }
}
