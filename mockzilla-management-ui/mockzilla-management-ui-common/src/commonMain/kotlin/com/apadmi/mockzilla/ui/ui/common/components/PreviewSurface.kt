package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode

@Composable
fun PreviewSurface(
    darkTheme: Boolean = LocalForceDarkMode.current || isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = CompositionLocalProvider(LocalForceDarkMode provides darkTheme) {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.surface, content = content)
    }
}
