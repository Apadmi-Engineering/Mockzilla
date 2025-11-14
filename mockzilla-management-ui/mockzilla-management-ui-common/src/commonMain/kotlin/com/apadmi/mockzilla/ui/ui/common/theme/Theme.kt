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

private val lightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    surfaceContainer = md_theme_light_surface_container,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

private val darkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

@get:Composable
val ColorScheme.success get() = when (LocalForceDarkMode.current || isSystemInDarkTheme()) {
    true -> StateColors(
        primary = Color(0xFF_00_E6_5F),
        container = Color(0xFF_00_82_36).copy(alpha = 0.1f),
    )
    false -> StateColors(
        primary = Color(0xFF_00_82_36),
        container = Color(0xFF_00_82_36).copy(alpha = 0.1f),
    )
}

@get:Composable
val ColorScheme.partialFailure get() = when (LocalForceDarkMode.current || isSystemInDarkTheme()) {
    true -> StateColors(
        primary = Color(0xFF_F0_90_00),
        container = Color(0xFF_D1_65_00).copy(alpha = 0.1f),
    )
    false -> StateColors(
        primary = Color(0xFF_D1_65_00),
        container = Color(0xFF_D1_65_00).copy(alpha = 0.1f),
    )
}

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
    val colors = if (useDarkTheme) {
        darkColors
    } else {
        lightColors
    }

    var scaleFactor by rememberSaveable { mutableFloatStateOf(ScaleFactor.default) }
    ProvideLocalisableStrings {
        CompositionLocalProvider(
            LocalSetScaleFactor provides { scale -> scaleFactor = scale },
        ) {
            ScaledDensity(scaleFactor = scaleFactor) {
                MaterialTheme(
                    colorScheme = colors,
                    content = content
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
        fontScale = currentDensity.fontScale * scaleFactor
    )
    CompositionLocalProvider(LocalDensity provides scaledDensity, content = content)
}
