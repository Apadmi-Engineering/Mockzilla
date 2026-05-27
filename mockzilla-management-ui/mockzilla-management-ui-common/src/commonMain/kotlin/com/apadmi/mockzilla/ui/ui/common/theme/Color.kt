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
val darkSurfaceSubtle = Color(0xFF111827)

val darkOnSurface = Color(0xFFF6F9FC)
val darkOnSurfaceVariant = Color(0xFFC0C4CB)
val darkOnSurfaceMuted = Color(0xFF888C94)
val darkOnSurfaceFaint = Color(0xFF5A5E65)

val darkOutline = Color(0xFF2B2E33)
val darkOutlineVariant = Color(0xFF373B41)

val darkPrimary = Color(0xFF68D5DA)
val darkInversePrimary = Color(0xFF00858D)
val darkOnPrimary = Color(0xFF07090D)
val darkPrimaryContainer = Color(0xFF0E2C2D)

val darkSuccess = Color(0xFF4ADE80)
val darkSuccessContainer = darkSuccess.copy(alpha = 0.14f)
val darkWarning = Color(0xFFCCB125)
val darkWarningContainer = darkWarning.copy(alpha = 0.14f)
val darkError = Color(0xFFFD736D)
val darkErrorContainer = Color(0xFF401312)
val darkTertiary = Color(0xFF38BDF8)
val darkTertiaryContainer = darkTertiary.copy(alpha = 0.14f)
val darkJsonKey = Color(0xFFA78BFA)

// ── Light theme ───────────────────────────────────────────────────────────────
val lightBackground = Color(0xFFF1F5F9)
val lightSurface = Color(0xFFFFFFFF)
val lightSurfaceContainer = Color(0xFFFFFFFF)
val lightSurfaceVariant = Color(0xFFF8FAFC)
val lightSurfaceMuted = Color(0xFFCBD5E1)
val lightSurfaceSubtle = Color(0xFFFFFFFF)

val lightOnSurface = Color(0xFF0E1217)
val lightOnSurfaceVariant = Color(0xFF2A2E34)
val lightOnSurfaceMuted = Color(0xFF494D55)
val lightOnSurfaceFaint = Color(0xFF646971)

val lightOutline = Color(0xFFC1C4C9)
val lightOutlineVariant = Color(0xFFA7ABB1)

val lightPrimary = Color(0xFF00858D)
val lightInversePrimary = Color(0xFF68D5DA)
val lightOnPrimary = Color(0xFFFAFCFF)
val lightPrimaryContainer = Color(0xFFC0E7E8)

val lightSuccess = Color(0xFF16A34A)
val lightSuccessContainer = lightSuccess.copy(alpha = 0.14f)
val lightWarning = Color(0xFFCA8A04)
val lightWarningContainer = lightWarning.copy(alpha = 0.14f)
val lightError = Color(0xFFC9222B)
val lightErrorContainer = Color(0xFFFFD6D1)
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
