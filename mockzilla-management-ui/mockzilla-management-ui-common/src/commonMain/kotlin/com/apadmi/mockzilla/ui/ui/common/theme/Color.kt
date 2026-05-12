@file:Suppress("MAGIC_NUMBER", "VARIABLE_NAME_INCORRECT_FORMAT", "LONG_NUMERICAL_VALUES_SEPARATED")

package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.ui.graphics.Color

// ── Dark theme ────────────────────────────────────────────────────────────────
val dark_bg0 = Color(0xFF020817) // app background
val dark_bg1 = Color(0xFF0B1220) // cards
val dark_bg2 = Color(0xFF111827)
val dark_bg3 = Color(0xFF1E293B)
val dark_bg4 = Color(0xFF1E293B)

val dark_fg0 = Color(0xFFF8FAFC) // main white text
val dark_fg1 = Color(0xFF94A3B8) // secondary text
val dark_fg2 = Color(0xFF64748B)
val dark_fg3 = Color(0xFF475569)

val dark_line1 = Color(0xFF172033)
val dark_line2 = Color(0xFF334155)

val dark_accent     = Color(0xFF00E5FF)
val dark_accent2    = Color(0xFF22D3EE)
val dark_accentFg   = Color(0xFF020817)
val dark_accentSoft = dark_accent.copy(alpha = 0.14f)

val dark_ok       = Color(0xFF4ADE80)
val dark_okSoft   = dark_ok.copy(alpha = 0.14f)
val dark_warn     = Color(0xFFFACC15)
val dark_warnSoft = dark_warn.copy(alpha = 0.14f)
val dark_err      = Color(0xFFF87171)
val dark_errSoft  = dark_err.copy(alpha = 0.14f)
val dark_info     = Color(0xFF38BDF8)
val dark_infoSoft = dark_info.copy(alpha = 0.14f)



// ── Light theme ───────────────────────────────────────────────────────────────
val light_bg0 = Color(0xFFF8FAFC) // app background
val light_bg1 = Color(0xFFFFFFFF) // cards
val light_bg2 = Color(0xFFFFFFFF)
val light_bg3 = Color(0xFFE2E8F0)
val light_bg4 = Color(0xFFCBD5E1)

val light_fg0 = Color(0xFF0F172A) // title text
val light_fg1 = Color(0xFF475569) // secondary text
val light_fg2 = Color(0xFF64748B)
val light_fg3 = Color(0xFF94A3B8)

val light_line1 = Color(0xFFE2E8F0)
val light_line2 = Color(0xFFCBD5E1)

val light_accent     = Color(0xFF06B6D4)
val light_accent2    = Color(0xFF0891B2)
val light_accentFg   = Color(0xFFFFFFFF)
val light_accentSoft = light_accent.copy(alpha = 0.14f)

val light_ok       = Color(0xFF16A34A)
val light_okSoft   = light_ok.copy(alpha = 0.14f)
val light_warn     = Color(0xFFCA8A04)
val light_warnSoft = light_warn.copy(alpha = 0.14f)
val light_err      = Color(0xFFDC2626)
val light_errSoft  = light_err.copy(alpha = 0.14f)
val light_info     = Color(0xFF0284C7)
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
