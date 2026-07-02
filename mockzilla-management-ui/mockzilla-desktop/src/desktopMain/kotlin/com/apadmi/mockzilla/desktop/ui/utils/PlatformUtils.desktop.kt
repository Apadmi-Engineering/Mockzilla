package com.apadmi.mockzilla.desktop.ui.utils

import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

actual fun isOsx(): Boolean = hostOs == OS.MacOS
actual fun isLinux(): Boolean = hostOs == OS.Linux
