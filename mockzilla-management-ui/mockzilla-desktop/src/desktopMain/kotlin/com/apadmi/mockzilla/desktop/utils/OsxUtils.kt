package com.apadmi.mockzilla.desktop.utils

import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import java.awt.Window

fun Window.handleOsxZoomBehaviour(state: WindowState, maxClickYy: Float) =
    addMouseListener(object : java.awt.event.MouseAdapter() {
        override fun mouseClicked(event: java.awt.event.MouseEvent) {
            if (event.clickCount == 2 && event.y < maxClickYy) {
                val isMax = state.placement == WindowPlacement.Maximized
                state.placement = if (isMax) WindowPlacement.Floating else WindowPlacement.Maximized
            }
        }
    })
