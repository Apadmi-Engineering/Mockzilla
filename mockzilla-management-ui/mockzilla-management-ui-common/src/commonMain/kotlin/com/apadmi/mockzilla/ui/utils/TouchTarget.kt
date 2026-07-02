package com.apadmi.mockzilla.ui.utils

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("MAGIC_NUMBER")
private val Platform.touchTargetSize: Dp
    get() = when (this) {
        Platform.Android, Platform.Ios -> 44.dp
        else -> 24.dp
    }

/** Enforces WCAG AA minimum touch target (44dp mobile, 24dp desktop).
 *
 * @param isIcon Whether to apply icon rules (i.e use the big version on JS)
 * @return*/
public fun Modifier.minimumTouchTarget(
    isIcon: Boolean = false
): Modifier = if (isIcon) {
    iconButtonSize()
} else {
    defaultMinSize(
        minWidth = Platform.current.touchTargetSize,
        minHeight = Platform.current.touchTargetSize,
    )
}

/**
 * Sets an exact size for a compact IconButton based on the platform
 *
 * @return
 */
public fun Modifier.iconButtonSize(): Modifier = size(
    when (Platform.current) {
        // Icons specifically use the mobile size since otherwise
        // they look shrunken
        Platform.Js -> Platform.Android.touchTargetSize
        else -> Platform.current.touchTargetSize
    }
)
