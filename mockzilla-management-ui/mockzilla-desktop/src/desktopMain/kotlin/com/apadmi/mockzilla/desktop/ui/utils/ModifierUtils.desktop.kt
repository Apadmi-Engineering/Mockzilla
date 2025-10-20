package com.apadmi.mockzilla.desktop.ui.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.onClick
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

actual fun Modifier.verticalResizeCursor(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))

actual fun Modifier.horizontalResizeCursor(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR)))

actual fun Modifier.mobileStatusBarPadding(): Modifier = this

@OptIn(ExperimentalFoundationApi::class)
actual fun Modifier.desktopTertiaryPointerClick(onClick: () -> Unit) = onClick(
    matcher = PointerMatcher.mouse(PointerButton.Tertiary),
    interactionSource = MutableInteractionSource(),
    onClick = onClick,
)
