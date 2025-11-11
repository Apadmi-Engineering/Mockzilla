package com.apadmi.mockzilla.lib.internal.utils

import kotlin.js.Date

internal actual fun epochMillis(): Long = Date.now().toLong()
