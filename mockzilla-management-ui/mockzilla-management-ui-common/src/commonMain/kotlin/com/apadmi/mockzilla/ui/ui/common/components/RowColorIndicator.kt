package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal fun Modifier.drawIndicator(color: Color) = drawBehind {
    val indicatorWidth = 3.dp.toPx()
    val yPadding = 4.dp.toPx()

    drawRoundRect(
        cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
        color = color,
        topLeft = Offset(0f, yPadding.dp.toPx()),
        size = Size(indicatorWidth, size.height - yPadding * 4)
    )
}