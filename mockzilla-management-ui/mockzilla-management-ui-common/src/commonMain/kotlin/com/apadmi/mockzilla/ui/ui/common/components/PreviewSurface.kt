package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode

@Composable
fun PreviewSurface(
    darkTheme: Boolean = LocalForceDarkMode.current || isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = AppTheme(useDarkTheme = darkTheme) {
    Surface(color = MaterialTheme.colorScheme.surface, content = content)
}
