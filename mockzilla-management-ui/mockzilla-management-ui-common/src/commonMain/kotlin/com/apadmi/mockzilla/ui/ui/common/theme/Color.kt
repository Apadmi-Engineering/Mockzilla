@file:Suppress(
    "MAGIC_NUMBER",
    "LONG_NUMERICAL_VALUES_SEPARATED"
)

package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.ui.graphics.Color

// ── Dark theme ────────────────────────────────────────────────────────────────
val darkBackground = Color(0xFF0B0D10)
val darkSurface = Color(0xFF0B0D10)
val darkSurfaceContainer = Color(0xFF111827)
val darkSurfaceVariant = Color(0xFF181B1F)
val darkSurfaceMuted = Color(0xFF1E293B)
val darkSurfaceSubtle = Color(0xFF111418)

val darkOnSurface = Color(0xFFF8FAFC)
val darkOnSurfaceVariant = Color(0xFF94A3B8)
val darkOnSurfaceMuted = Color(0xFF64748B)
val darkOnSurfaceFaint = Color(0xFF475569)

val darkOutline = Color(0xFF302C31)
val darkOutlineVariant = Color(0xFF00ECF8)

val darkPrimary = Color(0xFF00E5FF)
val darkInversePrimary = Color(0xFF22D3EE)
val darkOnPrimary = Color(0xFF020817)
val darkPrimaryContainer = darkPrimary.copy(alpha = 0.14f)

val darkSuccess = Color(0xFF4ADE80)
val darkSuccessContainer = darkSuccess.copy(alpha = 0.14f)
val darkWarning = Color(0xFFFACC15)
val darkWarningContainer = darkWarning.copy(alpha = 0.14f)
val darkError = Color(0xFFF87171)
val darkErrorContainer = darkError.copy(alpha = 0.14f)
val darkTertiary = Color(0xFF38BDF8)
val darkTertiaryContainer = darkTertiary.copy(alpha = 0.14f)
val darkJsonKey = Color(0xFFA78BFA)

// ── Light theme ───────────────────────────────────────────────────────────────
val lightBackground = Color(0xFFF8FAFC)
val lightSurface = Color(0xFFFFFFFF)
val lightSurfaceContainer = Color(0xFFF8FAFC)
val lightSurfaceVariant = Color(0xFFE2E8F0)
val lightSurfaceMuted = Color(0xFFCBD5E1)
val lightSurfaceSubtle = Color(0xFFFDFDFF)

val lightOnSurface = Color(0xFF0F172A)
val lightOnSurfaceVariant = Color(0xFF475569)
val lightOnSurfaceMuted = Color(0xFF64748B)
val lightOnSurfaceFaint = Color(0xFF94A3B8)

val lightOutline = Color(0xFFE2E8F0)
val lightOutlineVariant = Color(0xFFCBD5E1)

val lightPrimary = Color(0xFF06B6D4)
val lightInversePrimary = Color(0xFF0891B2)
val lightOnPrimary = Color(0xFFFFFFFF)
val lightPrimaryContainer = lightPrimary.copy(alpha = 0.14f)

val lightSuccess = Color(0xFF16A34A)
val lightSuccessContainer = lightSuccess.copy(alpha = 0.14f)
val lightWarning = Color(0xFFCA8A04)
val lightWarningContainer = lightWarning.copy(alpha = 0.14f)
val lightError = Color(0xFFDC2626)
val lightErrorContainer = lightError.copy(alpha = 0.14f)
val lightTertiary = Color(0xFF0284C7)
val lightTertiaryContainer = lightTertiary.copy(alpha = 0.14f)
val lightJsonKey = Color(0xFF7C3AED)

// ── HTTP method colours (shared; themed via ColorScheme extensions) ──────────
val darkMethodGet = Color(0xFF5ECDE8)
val darkMethodPost = Color(0xFF62D496)
val darkMethodPut = Color(0xFFD4C24C)
val darkMethodPatch = Color(0xFFD46ED4)
val darkMethodDelete = Color(0xFFD96050)
val darkMethodOther = Color(0xFF9898AC)

val lightMethodGet = Color(0xFF1A7A8A)
val lightMethodPost = Color(0xFF1E8248)
val lightMethodPut = Color(0xFF7A6A00)
val lightMethodPatch = Color(0xFF8A208A)
val lightMethodDelete = Color(0xFF9E2A1A)
val lightMethodOther = Color(0xFF5A5A70)
