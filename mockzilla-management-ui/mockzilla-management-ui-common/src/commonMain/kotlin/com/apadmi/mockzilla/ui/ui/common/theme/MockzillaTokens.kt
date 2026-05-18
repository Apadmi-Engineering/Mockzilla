package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class MockzillaTokens(
    // Background layers (0 = deepest, 4 = strongest surface)
    val bg0: Color, val bg1: Color, val bg2: Color, val bg3: Color, val bg4: Color, val bg5: Color,
    // Foreground / text
    val fg0: Color, val fg1: Color, val fg2: Color, val fg3: Color,
    // Borders
    val line1: Color, val line2: Color,
    // Interactive accent
    val accent: Color, val accent2: Color, val accentFg: Color, val accentSoft: Color,
    // Semantic status
    val ok: Color, val okSoft: Color,
    val warn: Color, val warnSoft: Color,
    val err: Color, val errSoft: Color,
    val info: Color, val infoSoft: Color,
    // HTTP method colours
    val methodGet: Color, val methodPost: Color, val methodPut: Color,
    val methodPatch: Color, val methodDelete: Color, val methodOther: Color,
) {
    companion object {
        val Dark = MockzillaTokens(
            bg0 = darkBackground,
            bg1 = darkSurface,
            bg2 = darkSurfaceContainer,
            bg3 = darkSurfaceVariant,
            bg4 = darkSurfaceMuted,
            bg5 = darkSurfaceSubtle,
            fg0 = darkOnSurface,
            fg1 = darkOnSurfaceVariant,
            fg2 = darkOnSurfaceMuted,
            fg3 = darkOnSurfaceFaint,
            line1 = darkOutline,
            line2 = darkOutlineVariant,
            accent = darkPrimary,
            accent2 = darkTertiary,
            accentFg = darkOnPrimary,
            accentSoft = darkPrimaryContainer,
            ok = darkSuccess,
            okSoft = darkSuccessContainer,
            warn = darkWarning,
            warnSoft = darkWarningContainer,
            err = darkError,
            errSoft = darkErrorContainer,
            info = darkTertiary,
            infoSoft = darkTertiaryContainer,
            methodGet = darkMethodGet,
            methodPost = darkMethodPost,
            methodPut = darkMethodPut,
            methodPatch = darkMethodPatch,
            methodDelete = darkMethodDelete,
            methodOther = darkMethodOther,
        )

        val Light = MockzillaTokens(
            bg0 = lightBackground,
            bg1 = lightSurface,
            bg2 = lightSurfaceContainer,
            bg3 = lightSurfaceVariant,
            bg4 = lightSurfaceMuted,
            bg5 = lightSurfaceSubtle,
            fg0 = lightOnSurface,
            fg1 = lightOnSurfaceVariant,
            fg2 = lightOnSurfaceMuted,
            fg3 = lightOnSurfaceFaint,
            line1 = lightOutline,
            line2 = lightOutlineVariant,
            accent = lightPrimary,
            accent2 = lightTertiary,
            accentFg = lightOnPrimary,
            accentSoft = lightPrimaryContainer,
            ok = lightSuccess,
            okSoft = lightSuccessContainer,
            warn = lightWarning,
            warnSoft = lightWarningContainer,
            err = lightError,
            errSoft = lightErrorContainer,
            info = lightTertiary,
            infoSoft = lightTertiaryContainer,
            methodGet = lightMethodGet,
            methodPost = lightMethodPost,
            methodPut = lightMethodPut,
            methodPatch = lightMethodPatch,
            methodDelete = lightMethodDelete,
            methodOther = lightMethodOther,
        )
    }
}

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalMockzillaTokens = compositionLocalOf { MockzillaTokens.Dark }
