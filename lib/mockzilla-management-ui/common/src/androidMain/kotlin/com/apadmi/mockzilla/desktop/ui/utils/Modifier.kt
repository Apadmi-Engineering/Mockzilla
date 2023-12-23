package com.apadmi.mockzilla.desktop.ui.utils

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier

actual fun Modifier.verticalResizeCursor(): Modifier = this
actual fun Modifier.horizontalResizeCursor(): Modifier = this
actual fun Modifier.androidStatusBarPadding() = statusBarsPadding()
