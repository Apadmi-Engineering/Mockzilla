package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget

fun MetaData.Companion.dummy() = MetaData(
    appName = "Jannie Bates",
    appPackage = "quod",
    operatingSystemVersion = "primis",
    deviceModel = "erat",
    appVersion = "1.0.0",
    runTarget = RunTarget.IosSimulator,
    mockzillaVersion = "1.0.0"
)
