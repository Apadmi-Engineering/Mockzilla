package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
@Composable
public actual fun StandardTextTooltip(text: String, content: @Composable () -> Unit) {
    /* Tooltips aren't a thing on Android devices */
    content()
}
