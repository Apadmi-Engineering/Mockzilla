package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_bold
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_medium
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_regular
import com.apadmi.mockzilla_management_ui_common.generated.resources.geist_semibold
import com.apadmi.mockzilla_management_ui_common.generated.resources.jetbrainsmono_medium
import com.apadmi.mockzilla_management_ui_common.generated.resources.jetbrainsmono_regular
import com.apadmi.mockzilla_management_ui_common.generated.resources.jetbrainsmono_semibold

import org.jetbrains.compose.resources.Font

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalMonoFontFamily = compositionLocalOf<FontFamily> { FontFamily.Monospace }

@Composable
fun mockzillaFontFamily(): FontFamily = FontFamily(
    Font(Res.font.geist_regular, weight = FontWeight.Normal),
    Font(Res.font.geist_medium, weight = FontWeight.Medium),
    Font(Res.font.geist_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.geist_bold, weight = FontWeight.Bold),
)

@Composable
fun mockzillaMonoFontFamily(): FontFamily = FontFamily(
    Font(Res.font.jetbrainsmono_regular, weight = FontWeight.Normal),
    Font(Res.font.jetbrainsmono_medium, weight = FontWeight.Medium),
    Font(Res.font.jetbrainsmono_semibold, weight = FontWeight.SemiBold),
)

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
fun mockzillaTypography(uiFont: FontFamily): Typography = Typography(
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
)
