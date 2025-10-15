package com.apadmi.mockzilla.ui.utils

import androidx.compose.ui.Modifier

fun Modifier.conditional(
    predicate: Boolean,
    positive: Modifier.() -> Modifier = { this },
    negative: Modifier.() -> Modifier = { this }
): Modifier = if (predicate) positive(this) else negative(this)
