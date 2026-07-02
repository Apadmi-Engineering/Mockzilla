package com.apadmi.mockzilla.ui.utils

import com.apadmi.mockzilla.lib.models.MetaData

public fun MetaData.prettyName(): String = let { "${it.appName} (${it.deviceModel})" }
