package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.internal.PlatformConfig

internal expect fun fetchAppIconBytes(platformConfig: PlatformConfig): ByteArray?
