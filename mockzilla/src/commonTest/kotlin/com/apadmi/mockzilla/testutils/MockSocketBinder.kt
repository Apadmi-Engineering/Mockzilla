package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.internal.utils.SocketBinder
import io.ktor.network.sockets.ServerSocket

internal expect class MockSocketBinder(whenBind: () -> ServerSocket) : SocketBinder
