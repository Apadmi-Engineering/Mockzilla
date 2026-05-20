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

private val darkColors = darkColorScheme(
    primary = darkPrimary,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnSurface,
    secondary = darkPrimary,
    onSecondary = darkOnPrimary,
    secondaryContainer = darkSurfaceVariant,
    onSecondaryContainer = darkOnSurface,
    tertiary = darkTertiary,
    onTertiary = darkBackground,
    tertiaryContainer = darkTertiaryContainer,
    onTertiaryContainer = darkOnSurface,
    error = darkError,
    errorContainer = darkErrorContainer,
    onError = darkBackground,
    onErrorContainer = darkError,
    background = darkBackground,
    onBackground = darkOnSurface,
    surface = darkSurface,
    onSurface = darkOnSurface,
    surfaceVariant = darkSurfaceVariant,
    surfaceContainer = darkSurfaceContainer,
    onSurfaceVariant = darkOnSurfaceVariant,
    outline = darkOutline,
    outlineVariant = darkOutlineVariant,
    inverseOnSurface = darkBackground,
    inverseSurface = darkOnSurface,
    inversePrimary = darkInversePrimary,
    scrim = Color.Black,
)

private val lightColors = lightColorScheme(
    primary = lightPrimary,
    onPrimary = lightOnPrimary,
    primaryContainer = lightPrimaryContainer,
    onPrimaryContainer = lightOnSurface,
    secondary = lightPrimary,
    onSecondary = lightOnPrimary,
    secondaryContainer = lightSurfaceVariant,
    onSecondaryContainer = lightOnSurface,
    tertiary = lightTertiary,
    onTertiary = lightOnSurface,
    tertiaryContainer = lightTertiaryContainer,
    onTertiaryContainer = lightOnSurface,
    error = lightError,
    errorContainer = lightErrorContainer,
    onError = lightOnSurface,
    onErrorContainer = lightError,
    background = lightBackground,
    onBackground = lightOnSurface,
    surface = lightSurface,
    onSurface = lightOnSurface,
    surfaceVariant = lightSurfaceVariant,
    surfaceContainer = lightSurfaceContainer,
    onSurfaceVariant = lightOnSurfaceVariant,
    outline = lightOutline,
    outlineVariant = lightOutlineVariant,
    inverseOnSurface = lightOnSurface,
    inverseSurface = lightBackground,
    inversePrimary = lightInversePrimary,
    scrim = Color.Black,
)

// ── Extensions for colours that don't map onto a Material slot ────────────────

@get:Composable
val ColorScheme.surfaceMuted: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkSurfaceMuted else lightSurfaceMuted

@get:Composable
val ColorScheme.surfaceSubtle: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkSurfaceSubtle else lightSurfaceSubtle

@get:Composable
val ColorScheme.onSurfaceMuted: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkOnSurfaceMuted else lightOnSurfaceMuted

@get:Composable
val ColorScheme.onSurfaceFaint: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkOnSurfaceFaint else lightOnSurfaceFaint

@get:Composable
val ColorScheme.success: StateColors
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) {
        StateColors(primary = darkSuccess, container = darkSuccessContainer)
    } else {
        StateColors(primary = lightSuccess, container = lightSuccessContainer)
    }

@get:Composable
val ColorScheme.warning: StateColors
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) {
        StateColors(primary = darkWarning, container = darkWarningContainer)
    } else {
        StateColors(primary = lightWarning, container = lightWarningContainer)
    }

@get:Composable
val ColorScheme.jsonKey: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkJsonKey else lightJsonKey

@get:Composable
val ColorScheme.methodGet: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkMethodGet else lightMethodGet

@get:Composable
val ColorScheme.methodPost: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkMethodPost else lightMethodPost

@get:Composable
val ColorScheme.methodPut: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkMethodPut else lightMethodPut

@get:Composable
val ColorScheme.methodPatch: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkMethodPatch else lightMethodPatch

@get:Composable
val ColorScheme.methodDelete: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkMethodDelete else lightMethodDelete

@get:Composable
val ColorScheme.methodOther: Color
    get() = if (LocalForceDarkMode.current || isSystemInDarkTheme()) darkMethodOther else lightMethodOther

/**
 * @property primary
 * @property container
 */
data class StateColors(
    val primary: Color,
    val container: Color
)

data object ScaleFactor {
    const val DEFAULT_DESKTOP = 1.0F
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
    val colorScheme = if (useDarkTheme) darkColors else lightColors
    val uiFont = mockzillaFontFamily()
    val monoFont = mockzillaMonoFontFamily()
    var scaleFactor by rememberSaveable { mutableFloatStateOf(ScaleFactor.default) }
    ProvideLocalisableStrings {
        CompositionLocalProvider(
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
