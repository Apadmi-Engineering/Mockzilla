package com.apadmi.mockzilla.desktop.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

actual fun Modifier.verticalResizeCursor(): Modifier = pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))
actual fun Modifier.horizontalResizeCursor(): Modifier = pointerHoverIcon(PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR)))
actual fun Modifier.androidStatusBarPadding(): Modifier = this
