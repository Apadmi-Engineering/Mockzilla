package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.lib.InternalMockzillaApi

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_bold
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_medium
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_regular
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_semibold
import com.apadmi.mockzilla_management_ui_common.generated.resources.jetbrainsmono_medium
import com.apadmi.mockzilla_management_ui_common.generated.resources.jetbrainsmono_regular
import com.apadmi.mockzilla_management_ui_common.generated.resources.jetbrainsmono_semibold
import org.jetbrains.compose.resources.Font

@InternalMockzillaApi
@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
public val LocalMonoFontFamily: ProvidableCompositionLocal<FontFamily> = compositionLocalOf<FontFamily> { FontFamily.Monospace }

@InternalMockzillaApi
@Composable
public fun mockzillaFontFamily(): FontFamily = if (rememberComposeFontsAvailable()) {
    FontFamily(
        Font(Res.font.geist_regular, weight = FontWeight.Normal),
        Font(Res.font.geist_medium, weight = FontWeight.Medium),
        Font(Res.font.geist_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.geist_bold, weight = FontWeight.Bold),
    )
} else {
    FontFamily.Default
}

@InternalMockzillaApi
@Composable
public fun mockzillaMonoFontFamily(): FontFamily = if (rememberComposeFontsAvailable()) {
    FontFamily(
        Font(Res.font.jetbrainsmono_regular, weight = FontWeight.Normal),
        Font(Res.font.jetbrainsmono_medium, weight = FontWeight.Medium),
        Font(Res.font.jetbrainsmono_semibold, weight = FontWeight.SemiBold),
    )
} else {
    FontFamily.Monospace
}

@InternalMockzillaApi
@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
public fun mockzillaTypography(uiFont: FontFamily): Typography = Typography(
    // Small utility labels — section headers, chips, monospace tags
    labelSmall = TextStyle(
        fontFamily = uiFont,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.06.em,
    ),
    labelMedium = TextStyle(
        fontFamily = uiFont,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
    ),
    labelLarge = TextStyle(
        fontFamily = uiFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
    ),
    // Body copy
    bodySmall = TextStyle(
        fontFamily = uiFont,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyMedium = TextStyle(
        fontFamily = uiFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyLarge = TextStyle(
        fontFamily = uiFont,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
    ),
    // Titles
    titleSmall = TextStyle(
        fontFamily = uiFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleMedium = TextStyle(
        fontFamily = uiFont,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleLarge = TextStyle(
        fontFamily = uiFont,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineSmall = TextStyle(
        fontFamily = uiFont,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineMedium = TextStyle(
        fontFamily = uiFont,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineLarge = TextStyle(
        fontFamily = uiFont,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
    ),
)

/**
 * Probes whether the bundled Compose font resources are actually present in the app bundle.
 *
 * On iOS the fonts are file-based Compose resources that must be copied into the consuming app's
 * bundle (via the `org.jetbrains.compose` Gradle plugin's resource step, CocoaPods `spec.resources`,
 * etc). If a consumer doesn't bundle them, the lazy [Font] loaders would otherwise throw
 * `MissingResourceException` during text layout and hard-crash the app. Reading the bytes up front
 * hits the same resource reader but is catchable, letting us fall back to system fonts instead.
 *
 * This only seems to apps that use KMP but not CMP
 *
 * See https://youtrack.jetbrains.com/issue/KT-66790.
 */
@Suppress("FUNCTION_BOOLEAN_PREFIX")
@Composable
private fun rememberComposeFontsAvailable(): Boolean {
    val available by produceState(initialValue = false) {
        value = runCatching { Res.readBytes("font/geist_regular.ttf") }.isSuccess
        if (value) {
            Logger.d(tag = "MockzillaFonts") {
                "Mockzilla compose font resources loaded; using bundled Geist/JetBrains Mono fonts."
            }
        } else {
            Logger.w(tag = "MockzillaFonts") {
                "Mockzilla compose font resources were not found in the app bundle - falling back to " +
                        "system fonts. This usually means the consuming iOS/KMP module isn't bundling " +
                        "compose-resources: apply the 'org.jetbrains.compose' Gradle plugin and embed the " +
                        "framework via embedAndSignAppleFrameworkForXcode / syncFramework. See KT-66790."
            }
        }
    }
    return available
}
