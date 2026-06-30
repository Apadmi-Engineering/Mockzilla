package com.apadmi.mockzilla.ui.utils

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("MAGIC_NUMBER")
private val touchTargetSize: Dp
    get() = when (Platform.current) {
        Platform.Android, Platform.Ios -> 44.dp
        else -> 24.dp
    }

/** Enforces WCAG AA minimum touch target (44dp mobile, 24dp desktop).
 *
 * @return*/
fun Modifier.minimumTouchTarget(): Modifier = defaultMinSize(
    minWidth = touchTargetSize,
    minHeight = touchTargetSize,
)

/**
 * Sets an exact size for a compact IconButton based on the platform
 *
 * @return
 */
fun Modifier.iconButtonSize(): Modifier = size(touchTargetSize)
