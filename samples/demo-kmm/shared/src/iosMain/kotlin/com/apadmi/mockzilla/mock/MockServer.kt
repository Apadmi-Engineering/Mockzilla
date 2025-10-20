package com.apadmi.mockzilla.mock

import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla
import com.apadmi.mockzilla.mobile.ui.launchManagementUi

fun stopMockServer() = stopMockzilla()
fun startMockServer() = startMockzilla(mockzillaConfig)
fun launchMockManagementUi() = launchManagementUi()
