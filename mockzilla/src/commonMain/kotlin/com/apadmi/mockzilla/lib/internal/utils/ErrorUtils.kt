@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
expect class AddressAlreadyInUseException : Throwable

@InternalMockzillaApi
fun Throwable.isSomeMatchInChain(predicate: (Throwable) -> Boolean): Boolean {
    var current: Throwable? = this
    while (current != null) {
        if (predicate(current)) {
            return true
        }
        current = current.cause
    }
    return false
}
