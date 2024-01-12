package com.apadmi.mockzilla.lib.internal.utils

import platform.Foundation.NSUUID

internal actual fun generateUuid(): String = NSUUID.UUID().UUIDString
