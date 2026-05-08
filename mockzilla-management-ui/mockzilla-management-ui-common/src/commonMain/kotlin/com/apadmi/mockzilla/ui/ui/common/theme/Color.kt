@file:Suppress("MAGIC_NUMBER", "VARIABLE_NAME_INCORRECT_FORMAT", "LONG_NUMERICAL_VALUES_SEPARATED")

package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.ui.graphics.Color

// ── Dark theme ────────────────────────────────────────────────────────────────
val dark_bg0 = Color(0xFF1E1E26)
val dark_bg1 = Color(0xFF232330)
val dark_bg2 = Color(0xFF292937)
val dark_bg3 = Color(0xFF303040)
val dark_bg4 = Color(0xFF3A3A4C)

val dark_fg0 = Color(0xFFF7F7FA)
val dark_fg1 = Color(0xFFC8C8D8)
val dark_fg2 = Color(0xFF9898AC)
val dark_fg3 = Color(0xFF6E6E80)

val dark_line1 = Color(0xCC39394B)
val dark_line2 = Color(0x9942425A)

val dark_accent    = Color(0xFF76D8CF)
val dark_accent2   = Color(0xFF5EC9BE)
val dark_accentFg  = Color(0xFF1C1C22)
val dark_accentSoft = dark_accent.copy(alpha = 0.14f)

val dark_ok       = Color(0xFF62D496)
val dark_okSoft   = dark_ok.copy(alpha = 0.14f)
val dark_warn     = Color(0xFFD4C24C)
val dark_warnSoft = dark_warn.copy(alpha = 0.14f)
val dark_err      = Color(0xFFD96050)
val dark_errSoft  = dark_err.copy(alpha = 0.14f)
val dark_info     = Color(0xFF6BBFE0)
val dark_infoSoft = dark_info.copy(alpha = 0.14f)

// ── Light theme ───────────────────────────────────────────────────────────────
val light_bg0 = Color(0xFFF3F3F7)
val light_bg1 = Color(0xFFEDEDF3)
val light_bg2 = Color(0xFFE5E5EE)
val light_bg3 = Color(0xFFD8D8E4)
val light_bg4 = Color(0xFFCACAD8)

val light_fg0 = Color(0xFF1E1E2A)
val light_fg1 = Color(0xFF3A3A4E)
val light_fg2 = Color(0xFF5A5A70)
val light_fg3 = Color(0xFF82829A)

val light_line1 = Color(0xCC919AAA)
val light_line2 = Color(0x99A5AAC0)

val light_accent    = Color(0xFF1A8A84)
val light_accent2   = Color(0xFF107870)
val light_accentFg  = Color(0xFFF7F7FA)
val light_accentSoft = light_accent.copy(alpha = 0.14f)

val light_ok       = Color(0xFF1E8248)
val light_okSoft   = light_ok.copy(alpha = 0.14f)
val light_warn     = Color(0xFF7A6A00)
val light_warnSoft = light_warn.copy(alpha = 0.14f)
val light_err      = Color(0xFF9E2A1A)
val light_errSoft  = light_err.copy(alpha = 0.14f)
val light_info     = Color(0xFF1A6A9E)
val light_infoSoft = light_info.copy(alpha = 0.14f)

// ── HTTP method colours (shared; themed via MockzillaTokens) ──────────────────
val method_get    = Color(0xFF5ECDE8)
val method_post   = Color(0xFF62D496)
val method_put    = Color(0xFFD4C24C)
val method_patch  = Color(0xFFD46ED4)
val method_delete = Color(0xFFD96050)
val method_other  = Color(0xFF9898AC)

val method_get_light    = Color(0xFF1A7A8A)
val method_post_light   = Color(0xFF1E8248)
val method_put_light    = Color(0xFF7A6A00)
val method_patch_light  = Color(0xFF8A208A)
val method_delete_light = Color(0xFF9E2A1A)
val method_other_light  = Color(0xFF5A5A70)
