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
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.i18n.ProvideLocalisableStrings
import com.apadmi.mockzilla.ui.utils.Platform

@InternalMockzillaApi
@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
public val LocalForceDarkMode: ProvidableCompositionLocal<Boolean> = compositionLocalOf { false }

@InternalMockzillaApi
@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
public val LocalSetForceDarkMode: ProvidableCompositionLocal<(Boolean) -> Unit> = compositionLocalOf { { /* noop */ } }

@InternalMockzillaApi
@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
public val LocalSetScaleFactor: ProvidableCompositionLocal<(Float) -> Unit> = compositionLocalOf { { /* noop */ } }

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

@InternalMockzillaApi
@get:Composable
public val ColorScheme.inputBackground: Color
    get() = if (LocalForceDarkMode.current) surface else surfaceVariant

@InternalMockzillaApi
@get:Composable
public val ColorScheme.onSurfaceMuted: Color
    get() = if (LocalForceDarkMode.current) darkOnSurfaceMuted else lightOnSurfaceMuted

@InternalMockzillaApi
@get:Composable
public val ColorScheme.onSurfaceFaint: Color
    get() = if (LocalForceDarkMode.current) darkOnSurfaceFaint else lightOnSurfaceFaint

@InternalMockzillaApi
@get:Composable
public val ColorScheme.chipSelectedBackground: Color
    get() = if (LocalForceDarkMode.current) surfaceContainerHigh else Color.White

@InternalMockzillaApi
@get:Composable
public val ColorScheme.success: StateColors
    get() = if (LocalForceDarkMode.current) {
        StateColors(primary = darkSuccess, container = darkSuccessContainer)
    } else {
        StateColors(primary = lightSuccess, container = lightSuccessContainer)
    }

@InternalMockzillaApi
@get:Composable
public val ColorScheme.warning: StateColors
    get() = if (LocalForceDarkMode.current) {
        StateColors(primary = darkWarning, container = darkWarningContainer)
    } else {
        StateColors(primary = lightWarning, container = lightWarningContainer)
    }

@InternalMockzillaApi
@get:Composable
public val ColorScheme.info: StateColors
    get() = if (LocalForceDarkMode.current) {
        StateColors(primary = darkInfo, container = darkInfoContainer)
    } else {
        StateColors(primary = lightInfo, container = lightInfoContainer)
    }

@InternalMockzillaApi
@get:Composable
public val ColorScheme.jsonKey: Color
    get() = if (LocalForceDarkMode.current) darkJsonKey else lightJsonKey

@get:Composable
internal val ColorScheme.jsonHighlight: JsonHighlightColors
    get() = JsonHighlightColors(
        keyColor = jsonKey,
        stringColor = success.primary,
        numberColor = tertiary,
        boolColor = warning.primary,
        nullColor = onSurfaceMuted
    )

@InternalMockzillaApi
@get:Composable
public val ColorScheme.methodGet: Color
    get() = if (LocalForceDarkMode.current) darkMethodGet else lightMethodGet

@InternalMockzillaApi
@get:Composable
public val ColorScheme.methodPost: Color
    get() = if (LocalForceDarkMode.current) darkMethodPost else lightMethodPost

@InternalMockzillaApi
@get:Composable
public val ColorScheme.methodPut: Color
    get() = if (LocalForceDarkMode.current) darkMethodPut else lightMethodPut

@InternalMockzillaApi
@get:Composable
public val ColorScheme.methodPatch: Color
    get() = if (LocalForceDarkMode.current) darkMethodPatch else lightMethodPatch

@InternalMockzillaApi
@get:Composable
public val ColorScheme.methodDelete: Color
    get() = if (LocalForceDarkMode.current) darkMethodDelete else lightMethodDelete

@InternalMockzillaApi
@get:Composable
public val ColorScheme.methodOther: Color
    get() = if (LocalForceDarkMode.current) darkMethodOther else lightMethodOther

@InternalMockzillaApi
public data class StateColors(
    val primary: Color,
    val container: Color
)

@InternalMockzillaApi
public data object ScaleFactor {
    public const val DEFAULT_DESKTOP: Float = 1.0F
    public const val DEFAULT_MOBILE: Float = 1.0F
    public val default: Float = when (Platform.current) {
        Platform.Android, Platform.Ios -> DEFAULT_MOBILE
        Platform.Desktop -> DEFAULT_DESKTOP
        else -> DEFAULT_MOBILE
    }
}

/**
 * @property keyColor Color for JSON object keys.
 * @property stringColor Color for JSON string values.
 * @property numberColor Color for JSON numeric values.
 * @property boolColor Color for JSON boolean values.
 * @property nullColor Color for JSON null values.
 */
internal data class JsonHighlightColors(
    val keyColor: Color,
    val stringColor: Color,
    val numberColor: Color,
    val boolColor: Color,
    val nullColor: Color,
)

@InternalMockzillaApi
@Composable
public fun AppTheme(
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

@InternalMockzillaApi
@Composable
public fun ScaledDensity(scaleFactor: Float, content: @Composable () -> Unit) {
    val currentDensity = LocalDensity.current
    val scaledDensity = Density(
        density = currentDensity.density * scaleFactor,
        fontScale = currentDensity.fontScale * scaleFactor,
    )
    CompositionLocalProvider(LocalDensity provides scaledDensity, content = content)
}
