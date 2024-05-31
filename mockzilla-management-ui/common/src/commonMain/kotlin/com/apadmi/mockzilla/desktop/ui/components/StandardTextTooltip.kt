package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun StandardTextTooltip(text: String, content: @Composable () -> Unit)