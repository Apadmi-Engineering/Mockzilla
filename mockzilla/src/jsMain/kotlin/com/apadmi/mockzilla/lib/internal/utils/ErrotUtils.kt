@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import kotlinx.io.IOException

@InternalMockzillaApi
actual typealias AddressAlreadyInUseException = DummyException

// This will never actually happen since on JS multiple addresses aren't used
@InternalMockzillaApi
class DummyException : IOException()
