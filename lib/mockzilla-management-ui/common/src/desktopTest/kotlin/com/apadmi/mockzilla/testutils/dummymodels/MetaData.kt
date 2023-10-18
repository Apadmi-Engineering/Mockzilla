package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.lib.models.MetaData

fun MetaData.Companion.dummy() = MetaData(
    appName = "Jannie Bates",
    appPackage = "quod",
    operatingSystemVersion = "primis",
    deviceModel = "erat",
    appVersion = "ante",
    operatingSystem = "quot",
    mockzillaVersion = "dolorem"
)