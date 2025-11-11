package com.apadmi.mockzilla.lib.internal.utils

internal actual fun generateUuid() = js("crypto.randomUUID()").toString()
