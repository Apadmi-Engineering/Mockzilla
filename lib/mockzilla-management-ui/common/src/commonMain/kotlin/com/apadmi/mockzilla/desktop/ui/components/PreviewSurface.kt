package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.apadmi.mockzilla.desktop.ui.theme.AppTheme

@Composable
fun PreviewSurface(
    color: Color = Color.DarkGray,
    content: @Composable () -> Unit
) = AppTheme {
    Surface(color = color, content = content)
}
