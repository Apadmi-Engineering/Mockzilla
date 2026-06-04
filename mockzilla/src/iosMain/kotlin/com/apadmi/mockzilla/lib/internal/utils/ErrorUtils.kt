@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import io.ktor.utils.io.errors.PosixException.AddressAlreadyInUseException as KtorAddressAlreadyInUseException

@InternalMockzillaApi
actual typealias AddressAlreadyInUseException = KtorAddressAlreadyInUseException
