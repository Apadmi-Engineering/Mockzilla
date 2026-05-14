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

/**
 * @property primary
 * @property container
 */
data class StateColors(
    val primary: Color,
    val container: Color
)

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

private fun buildDarkColorScheme(tokens: MockzillaTokens): ColorScheme = darkColorScheme(
    primary = tokens.accent,
    onPrimary = tokens.accentFg,
    primaryContainer = tokens.accentSoft,
    onPrimaryContainer = tokens.fg0,
    secondary = tokens.accent,
    onSecondary = tokens.accentFg,
    secondaryContainer = tokens.bg3,
    onSecondaryContainer = tokens.fg0,
    tertiary = tokens.info,
    onTertiary = tokens.bg0,
    tertiaryContainer = tokens.infoSoft,
    onTertiaryContainer = tokens.fg0,
    error = tokens.err,
    errorContainer = tokens.errSoft,
    onError = tokens.bg0,
    onErrorContainer = tokens.err,
    background = tokens.bg0,
    onBackground = tokens.fg0,
    surface = tokens.bg1,
    onSurface = tokens.fg0,
    surfaceVariant = tokens.bg3,
    surfaceContainer = tokens.bg2,
    onSurfaceVariant = tokens.fg1,
    outline = tokens.line1,
    outlineVariant = tokens.line2,
    inverseOnSurface = tokens.bg0,
    inverseSurface = tokens.fg0,
    inversePrimary = tokens.accent2,
    scrim = Color.Black,
)

private fun buildLightColorScheme(tokens: MockzillaTokens): ColorScheme = lightColorScheme(
    primary = tokens.accent,
    onPrimary = tokens.accentFg,
    primaryContainer = tokens.accentSoft,
    onPrimaryContainer = tokens.fg0,
    secondary = tokens.accent,
    onSecondary = tokens.accentFg,
    secondaryContainer = tokens.bg3,
    onSecondaryContainer = tokens.fg0,
    tertiary = tokens.info,
    onTertiary = tokens.fg0,
    tertiaryContainer = tokens.infoSoft,
    onTertiaryContainer = tokens.fg0,
    error = tokens.err,
    errorContainer = tokens.errSoft,
    onError = tokens.fg0,
    onErrorContainer = tokens.err,
    background = tokens.bg0,
    onBackground = tokens.fg0,
    surface = tokens.bg1,
    onSurface = tokens.fg0,
    surfaceVariant = tokens.bg3,
    surfaceContainer = tokens.bg2,
    onSurfaceVariant = tokens.fg1,
    outline = tokens.line1,
    outlineVariant = tokens.line2,
    inverseOnSurface = tokens.fg0,
    inverseSurface = tokens.bg0,
    inversePrimary = tokens.accent2,
    scrim = Color.Black,
)
