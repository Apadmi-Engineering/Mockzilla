package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun StandardTextTooltip(text: String, content: @Composable () -> Unit) {
    /* Tooltips aren't a thing on Android devices */
    content()
}