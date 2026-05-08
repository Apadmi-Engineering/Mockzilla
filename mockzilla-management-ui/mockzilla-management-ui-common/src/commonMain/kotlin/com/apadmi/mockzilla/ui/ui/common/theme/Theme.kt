@file:Suppress("MAGIC_NUMBER", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

import com.apadmi.mockzilla.ui.i18n.ProvideLocalisableStrings
import com.apadmi.mockzilla.ui.utils.Platform

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalForceDarkMode = compositionLocalOf { false }

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalSetScaleFactor = compositionLocalOf<(Float) -> Unit> { { /* noop */ } }

data object ScaleFactor {
    const val DEFAULT_DESKTOP = 0.9F
    const val DEFAULT_MOBILE = 1.0F
    val default = when (Platform.current) {
        Platform.Android, Platform.Ios -> DEFAULT_MOBILE
        Platform.Desktop -> DEFAULT_DESKTOP
        else -> DEFAULT_MOBILE
    }
}

@Composable
fun Modifier.alternatingBackground(index: Int) = background(
    if (index % 2 == 0) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.background
    }
)

private fun buildDarkColorScheme(t: MockzillaTokens): ColorScheme = darkColorScheme(
    primary = t.accent,
    onPrimary = t.accentFg,
    primaryContainer = t.accentSoft,
    onPrimaryContainer = t.fg0,
    secondary = t.accent,
    onSecondary = t.accentFg,
    secondaryContainer = t.bg3,
    onSecondaryContainer = t.fg0,
    tertiary = t.info,
    onTertiary = t.bg0,
    tertiaryContainer = t.infoSoft,
    onTertiaryContainer = t.fg0,
    error = t.err,
    errorContainer = t.errSoft,
    onError = t.bg0,
    onErrorContainer = t.err,
    background = t.bg0,
    onBackground = t.fg0,
    surface = t.bg1,
    onSurface = t.fg0,
    surfaceVariant = t.bg3,
    surfaceContainer = t.bg2,
    onSurfaceVariant = t.fg1,
    outline = t.line1,
    outlineVariant = t.line2,
    inverseOnSurface = t.bg0,
    inverseSurface = t.fg0,
    inversePrimary = t.accent2,
    scrim = Color.Black,
)

private fun buildLightColorScheme(t: MockzillaTokens): ColorScheme = lightColorScheme(
    primary = t.accent,
    onPrimary = t.accentFg,
    primaryContainer = t.accentSoft,
    onPrimaryContainer = t.fg0,
    secondary = t.accent,
    onSecondary = t.accentFg,
    secondaryContainer = t.bg3,
    onSecondaryContainer = t.fg0,
    tertiary = t.info,
    onTertiary = t.fg0,
    tertiaryContainer = t.infoSoft,
    onTertiaryContainer = t.fg0,
    error = t.err,
    errorContainer = t.errSoft,
    onError = t.fg0,
    onErrorContainer = t.err,
    background = t.bg0,
    onBackground = t.fg0,
    surface = t.bg1,
    onSurface = t.fg0,
    surfaceVariant = t.bg3,
    surfaceContainer = t.bg2,
    onSurfaceVariant = t.fg1,
    outline = t.line1,
    outlineVariant = t.line2,
    inverseOnSurface = t.fg0,
    inverseSurface = t.bg0,
    inversePrimary = t.accent2,
    scrim = Color.Black,
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = LocalForceDarkMode.current || isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val tokens = if (useDarkTheme) MockzillaTokens.Dark else MockzillaTokens.Light
    val colorScheme = if (useDarkTheme) buildDarkColorScheme(tokens) else buildLightColorScheme(tokens)
    val uiFont = mockzillaFontFamily()
    val monoFont = mockzillaMonoFontFamily()
    var scaleFactor by rememberSaveable { mutableFloatStateOf(ScaleFactor.default) }
    ProvideLocalisableStrings {
        CompositionLocalProvider(
            LocalMockzillaTokens provides tokens,
            LocalMonoFontFamily provides monoFont,
            LocalSetScaleFactor provides { scale -> scaleFactor = scale },
        ) {
            ScaledDensity(scaleFactor = scaleFactor) {
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = mockzillaTypography(uiFont),
                    content = content,
                )
            }
        }
    }
}

@Composable
fun ScaledDensity(scaleFactor: Float, content: @Composable () -> Unit) {
    val currentDensity = LocalDensity.current
    val scaledDensity = Density(
        density = currentDensity.density * scaleFactor,
        fontScale = currentDensity.fontScale * scaleFactor,
    )
    CompositionLocalProvider(LocalDensity provides scaledDensity, content = content)
}
