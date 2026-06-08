package com.apadmi.mockzilla.desktop.utils

import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import java.awt.Window

fun Window.handleOsxZoomBehaviour(state: WindowState) = addMouseListener(object : java.awt.event.MouseAdapter() {
    override fun mouseClicked(e: java.awt.event.MouseEvent) {
        if (e.clickCount == 2 && e.y < 40) {
            val isMax = state.placement == WindowPlacement.Maximized
            state.placement = if (isMax) WindowPlacement.Floating else WindowPlacement.Maximized
        }
    }
})
