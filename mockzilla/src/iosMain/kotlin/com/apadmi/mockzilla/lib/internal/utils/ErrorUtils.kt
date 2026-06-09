@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.utils

import io.ktor.utils.io.errors.PosixException.AddressAlreadyInUseException as KtorAddressAlreadyInUseException

internal actual typealias AddressAlreadyInUseException = KtorAddressAlreadyInUseException
