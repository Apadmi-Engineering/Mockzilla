package com.apadmi.mockzilla.desktop.utils

import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import java.awt.Window

private const val maxZoomClickY = 40  // Rough guess at the toolbar height in pixels, won't be accurate but better than nothing
fun Window.handleOsxZoomBehaviour(state: WindowState) = addMouseListener(object : java.awt.event.MouseAdapter() {
    override fun mouseClicked(event: java.awt.event.MouseEvent) {
        if (event.clickCount == 2 && event.y < maxZoomClickY) {
            val isMax = state.placement == WindowPlacement.Maximized
            state.placement = if (isMax) WindowPlacement.Floating else WindowPlacement.Maximized
        }
    }
})
