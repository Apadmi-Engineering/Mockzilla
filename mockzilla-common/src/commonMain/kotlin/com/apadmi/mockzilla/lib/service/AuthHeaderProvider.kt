package com.apadmi.mockzilla.lib.service

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

interface AuthHeaderProvider {
    suspend fun generateHeader(): Header
    /**
     * @property key
     * @property value
     */
    data class Header(val key: String, val value: String)
}
