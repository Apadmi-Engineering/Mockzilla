package com.apadmi.mockzilla.desktop.ui.utils

import androidx.compose.ui.Modifier

expect fun Modifier.verticalResizeCursor(): Modifier
expect fun Modifier.horizontalResizeCursor(): Modifier
expect fun Modifier.mobileStatusBarPadding(): Modifier
expect fun Modifier.desktopTertiaryPointerClick(onClick: () -> Unit): Modifier
