package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class MockzillaTokens(
    // Background layers (0 = deepest, 4 = strongest surface)
    val bg0: Color, val bg1: Color, val bg2: Color, val bg3: Color, val bg4: Color,
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
            bg0 = dark_bg0, bg1 = dark_bg1, bg2 = dark_bg2, bg3 = dark_bg3, bg4 = dark_bg4,
            fg0 = dark_fg0, fg1 = dark_fg1, fg2 = dark_fg2, fg3 = dark_fg3,
            line1 = dark_line1, line2 = dark_line2,
            accent = dark_accent, accent2 = dark_accent2, accentFg = dark_accentFg, accentSoft = dark_accentSoft,
            ok = dark_ok, okSoft = dark_okSoft,
            warn = dark_warn, warnSoft = dark_warnSoft,
            err = dark_err, errSoft = dark_errSoft,
            info = dark_info, infoSoft = dark_infoSoft,
            methodGet = method_get, methodPost = method_post, methodPut = method_put,
            methodPatch = method_patch, methodDelete = method_delete, methodOther = method_other,
        )

        val Light = MockzillaTokens(
            bg0 = light_bg0, bg1 = light_bg1, bg2 = light_bg2, bg3 = light_bg3, bg4 = light_bg4,
            fg0 = light_fg0, fg1 = light_fg1, fg2 = light_fg2, fg3 = light_fg3,
            line1 = light_line1, line2 = light_line2,
            accent = light_accent, accent2 = light_accent2, accentFg = light_accentFg, accentSoft = light_accentSoft,
            ok = light_ok, okSoft = light_okSoft,
            warn = light_warn, warnSoft = light_warnSoft,
            err = light_err, errSoft = light_errSoft,
            info = light_info, infoSoft = light_infoSoft,
            methodGet = method_get_light, methodPost = method_post_light, methodPut = method_put_light,
            methodPatch = method_patch_light, methodDelete = method_delete_light, methodOther = method_other_light,
        )
    }
}

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalMockzillaTokens = compositionLocalOf { MockzillaTokens.Dark }
