package com.apadmi.mockzilla.ui.utils

import androidx.compose.ui.input.pointer.PointerIcon

/**
 * A pointer icon that signals an action is blocked/unavailable.
 * On desktop this renders as a ⊘ cursor; on other platforms falls back to [PointerIcon.Default].
 */
expect val blockedPointerIcon: PointerIcon
