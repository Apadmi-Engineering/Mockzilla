package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.runtime.Composable

@Composable
expect fun StandardTextTooltip(text: String, content: @Composable () -> Unit)
