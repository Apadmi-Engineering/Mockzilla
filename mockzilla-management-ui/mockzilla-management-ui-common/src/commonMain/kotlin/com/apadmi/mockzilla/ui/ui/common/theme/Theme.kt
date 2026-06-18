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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

import com.apadmi.mockzilla.ui.i18n.ProvideLocalisableStrings
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.ALPHA_MUTED
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.JsonHighlightColors
import com.apadmi.mockzilla.ui.utils.Platform

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalForceDarkMode = compositionLocalOf { false }

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalSetForceDarkMode = compositionLocalOf<(Boolean) -> Unit> { { /* noop */ } }

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalSetScaleFactor = compositionLocalOf<(Float) -> Unit> { { /* noop */ } }

private val darkColors = darkColorScheme(
    primary = darkPrimary,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnSurface,
    secondary = darkPrimary,
    onSecondary = darkOnPrimary,
    secondaryContainer = darkVariant,
    onSecondaryContainer = darkOnSurface,
    tertiary = darkMethodPatch,
    onTertiary = darkBackground,
    tertiaryContainer = darkVariant,
    onTertiaryContainer = darkOnSurface,
    error = darkError,
    errorContainer = darkErrorContainer,
    onError = darkBackground,
    onErrorContainer = darkError,
    background = darkBackground,
    onBackground = darkOnSurface,
    surface = darkSurface,
    onSurface = darkOnSurface,
    surfaceVariant = darkVariant,
    surfaceContainer = darkContainer,
    surfaceContainerLow = darkSurface,
    surfaceContainerLowest = darkBackground,
    surfaceContainerHigh = darkVariant,
    surfaceContainerHighest = darkElevated,
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
    secondaryContainer = lightVariant,
    onSecondaryContainer = lightOnSurface,
    tertiary = lightMethodPatch,
    onTertiary = lightOnSurface,
    tertiaryContainer = lightVariant,
    onTertiaryContainer = lightOnSurface,
    error = lightError,
    errorContainer = lightErrorContainer,
    onError = lightOnSurface,
    onErrorContainer = lightError,
    background = lightBackground,
    onBackground = lightOnSurface,
    surface = lightSurface,
    onSurface = lightOnSurface,
    surfaceVariant = lightVariant,
    surfaceContainer = lightContainer,
    surfaceContainerLow = lightSurface,
    surfaceContainerLowest = lightBackground,
    surfaceContainerHigh = lightVariant,
    surfaceContainerHighest = lightElevated,
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
val ColorScheme.inputBackground: Color
    get() = if (LocalForceDarkMode.current) surface else surfaceVariant

@get:Composable
val ColorScheme.onSurfaceMuted: Color
    get() = if (LocalForceDarkMode.current) darkOnSurfaceMuted else lightOnSurfaceMuted

@get:Composable
val ColorScheme.onSurfaceFaint: Color
    get() = if (LocalForceDarkMode.current) darkOnSurfaceFaint else lightOnSurfaceFaint

@get:Composable
val ColorScheme.chipSelectedBackground: Color
    get() = if (LocalForceDarkMode.current) surfaceContainerHigh else Color.White

@get:Composable
val ColorScheme.success: StateColors
    get() = if (LocalForceDarkMode.current) {
        StateColors(primary = darkSuccess, container = darkSuccessContainer)
    } else {
        StateColors(primary = lightSuccess, container = lightSuccessContainer)
    }

@get:Composable
val ColorScheme.warning: StateColors
    get() = if (LocalForceDarkMode.current) {
        StateColors(primary = darkWarning, container = darkWarningContainer)
    } else {
        StateColors(primary = lightWarning, container = lightWarningContainer)
    }

@get:Composable
val ColorScheme.info: StateColors
    get() = if (LocalForceDarkMode.current) {
        StateColors(primary = darkInfo, container = darkInfoContainer)
    } else {
        StateColors(primary = lightInfo, container = lightInfoContainer)
    }

@get:Composable
val ColorScheme.jsonKey: Color
    get() = if (LocalForceDarkMode.current) darkJsonKey else lightJsonKey

@get:Composable
internal val ColorScheme.jsonHighlight: JsonHighlightColors
    get() = JsonHighlightColors(
        keyColor = jsonKey,
        stringColor = success.primary,
        numberColor = tertiary,
        boolColor = warning.primary,
        nullColor = onSurface.copy(alpha = ALPHA_MUTED)
    )

@get:Composable
val ColorScheme.methodGet: Color
    get() = if (LocalForceDarkMode.current) darkMethodGet else lightMethodGet

@get:Composable
val ColorScheme.methodPost: Color
    get() = if (LocalForceDarkMode.current) darkMethodPost else lightMethodPost

@get:Composable
val ColorScheme.methodPut: Color
    get() = if (LocalForceDarkMode.current) darkMethodPut else lightMethodPut

@get:Composable
val ColorScheme.methodPatch: Color
    get() = if (LocalForceDarkMode.current) darkMethodPatch else lightMethodPatch

@get:Composable
val ColorScheme.methodDelete: Color
    get() = if (LocalForceDarkMode.current) darkMethodDelete else lightMethodDelete

@get:Composable
val ColorScheme.methodOther: Color
    get() = if (LocalForceDarkMode.current) darkMethodOther else lightMethodOther

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
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val uiFont = mockzillaFontFamily()
    val monoFont = mockzillaMonoFontFamily()
    var scaleFactor by rememberSaveable { mutableFloatStateOf(ScaleFactor.default) }
    var forceDarkMode by rememberSaveable { mutableStateOf(useDarkTheme) }
    ProvideLocalisableStrings {
        CompositionLocalProvider(
            LocalForceDarkMode provides forceDarkMode,
            LocalSetForceDarkMode provides { forceDarkMode = it },
            LocalMonoFontFamily provides monoFont,
            LocalSetScaleFactor provides { scale -> scaleFactor = scale },
        ) {
            ScaledDensity(scaleFactor = scaleFactor) {
                MaterialTheme(
                    colorScheme = if (forceDarkMode) darkColors else lightColors,
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
