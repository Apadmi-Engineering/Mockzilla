package com.apadmi.mockzilla.lib.internal.utils

import java.util.UUID

internal actual fun generateUuid() = UUID.randomUUID().toString()
