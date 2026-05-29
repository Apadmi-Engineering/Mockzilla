@file:Suppress(
    "MAGIC_NUMBER",
    "LONG_NUMERICAL_VALUES_SEPARATED"
)

package com.apadmi.mockzilla.ui.ui.common.theme

import androidx.compose.ui.graphics.Color

// ── Surface tones ────────────────────────────────────────────────────────────
internal val darkBackground = Color(0xFF0B0D11)  // T0
internal val darkSurface = Color(0xFF111418)  // T1
internal val darkContainer = Color(0xFF181B1F)  // T2
internal val darkVariant = Color(0xFF21242A)  // T3
internal val darkElevated = Color(0xFF2D3137)  // T4

internal val lightBackground = Color(0xFFE9EBEE)  // T0
internal val lightSurface = Color(0xFFFDFDFF)  // T1
internal val lightContainer = Color(0xFFF2F3F6)  // T2
internal val lightVariant = Color(0xFFE2E5E9)  // T3
internal val lightElevated = Color(0xFFCBCED3)  // T4

// ── On-surface text ──────────────────────────────────────────────────────────
internal val darkOnSurface = Color(0xFFF6F9FC)
internal val darkOnSurfaceVariant = Color(0xFFC0C4CB)
internal val darkOnSurfaceMuted = Color(0xFF888C94)
internal val darkOnSurfaceFaint = Color(0xFF5A5E65)

internal val lightOnSurface = Color(0xFF0E1217)
internal val lightOnSurfaceVariant = Color(0xFF2A2E34)
internal val lightOnSurfaceMuted = Color(0xFF494D55)
internal val lightOnSurfaceFaint = Color(0xFF646971)

// ── Outline ──────────────────────────────────────────────────────────────────
internal val darkOutline = Color(0xFF2B2E33)
internal val darkOutlineVariant = Color(0xFF373B41)
internal val lightOutline = Color(0xFFC1C4C9)
internal val lightOutlineVariant = Color(0xFFA7ABB1)

// ── Primary (Mockzilla teal) ─────────────────────────────────────────────────
internal val darkPrimary = Color(0xFF00ECF8)
internal val darkOnPrimary = Color(0xFF07090D)
internal val darkPrimaryContainer = Color(0xFF0E2C2D)
internal val darkInversePrimary = Color(0xFF00858D)

internal val lightPrimary = Color(0xFF00858D)
internal val lightOnPrimary = Color(0xFFFAFCFF)
internal val lightPrimaryContainer = Color(0xFFC0E7E8)
internal val lightInversePrimary = Color(0xFF68D5DA)

// ── State colours ────────────────────────────────────────────────────────────
internal val darkSuccess = Color(0xFF61DA92)
internal val darkSuccessContainer = Color(0xFF092917)
internal val darkWarning = Color(0xFFF3B94C)
internal val darkWarningContainer = Color(0xFF312103)
internal val darkError = Color(0xFFFD736D)
internal val darkErrorContainer = Color(0xFF401312)
internal val darkInfo = Color(0xFF59C5F5)
internal val darkInfoContainer = Color(0xFF002635)

internal val lightSuccess = Color(0xFF007C36)
internal val lightSuccessContainer = Color(0xFFC0F3D0)
internal val lightWarning = Color(0xFFB16600)
internal val lightWarningContainer = Color(0xFFFFE1B6)
internal val lightError = Color(0xFFC9222B)
internal val lightErrorContainer = Color(0xFFFFD6D1)
internal val lightInfo = Color(0xFF0075A9)
internal val lightInfoContainer = Color(0xFFC4ECFF)

// ── HTTP methods ─────────────────────────────────────────────────────────────
internal val darkMethodGet = Color(0xFF00DFE8)
internal val darkMethodPost = Color(0xFF61DA92)
internal val darkMethodPut = Color(0xFFF3B94C)
internal val darkMethodPatch = Color(0xFFCEA4FF)
internal val darkMethodDelete = Color(0xFFFD736D)
internal val darkMethodOther = Color(0xFF888C94)

internal val lightMethodGet = Color(0xFF008892)
internal val lightMethodPost = Color(0xFF007C36)
internal val lightMethodPut = Color(0xFFB76C00)
internal val lightMethodPatch = Color(0xFF794DB6)
internal val lightMethodDelete = Color(0xFFC9222B)
internal val lightMethodOther = Color(0xFF494D55)

// ── JSON syntax ──────────────────────────────────────────────────────────────
internal val darkJsonKey = Color(0xFFCEA4FF)
internal val lightJsonKey = Color(0xFF794DB6)
