@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.utils

import androidx.compose.ui.input.pointer.PointerIcon
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Point
import java.awt.RenderingHints
import java.awt.Toolkit
import java.awt.image.BufferedImage

actual val blockedPointerIcon: PointerIcon by lazy {
    try {
        val size = 24
        val img = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val gr = img.createGraphics()
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        gr.color = Color(140, 140, 145, 220)
        gr.stroke = BasicStroke(2.5f)
        // Circle
        gr.drawOval(2, 2, size - 4, size - 4)
        // Diagonal line (top-right to bottom-left for ⊘)
        gr.drawLine(size - 6, 6, 6, size - 6)
        gr.dispose()
        val cursor = Toolkit.getDefaultToolkit()
            .createCustomCursor(img, Point(size / 2, size / 2), "blocked")
        PointerIcon(cursor)
    } catch (_: Exception) {
        PointerIcon.Default
    }
}
