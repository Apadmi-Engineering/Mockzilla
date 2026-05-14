package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.models.PortConflictException

internal suspend fun <T> runHandlingPortConflict(port: Int, block: suspend () -> T): T {
    try {
        return block()
    } catch (e: Exception) {
        if (e.isSomeMatchInChain { it is AddressAlreadyInUseException }) {
            throw PortConflictException(port, e)
        } else {
            throw e
        }
    }
}