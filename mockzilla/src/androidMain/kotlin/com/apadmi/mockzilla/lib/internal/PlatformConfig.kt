package com.apadmi.mockzilla.lib.internal

import android.content.Context

internal actual class PlatformConfig() {
    internal var context: Context? = null

    constructor(context: Context) : this() {
        this.context = context
    }
}
