package com.apadmi.mockzilla.ui.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.MetaData

@InternalMockzillaApi
public fun MetaData.prettyName(): String = let { "${it.appName} (${it.deviceModel})" }
