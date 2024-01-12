package com.apadmi.mockzilla

import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla

fun stopMockServer() = stopMockzilla()
fun startMockServer() = startMockzilla(mockzillaConfig)
