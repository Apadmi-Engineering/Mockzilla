@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.io.IOException

actual typealias AddressAlreadyInUseException = DummyException

// This will never actually happen since on JS multiple addresses aren't used
class DummyException : IOException()
