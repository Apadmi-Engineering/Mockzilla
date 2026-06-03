package com.apadmi.mockzilla.ui.utils

import com.apadmi.mockzilla.lib.models.MetaData

fun MetaData.prettyName() = let { "${it.appName} (${it.deviceModel})" }
