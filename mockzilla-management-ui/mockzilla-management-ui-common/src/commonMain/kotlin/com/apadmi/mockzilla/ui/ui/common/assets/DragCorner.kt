@file:Suppress("diktat") // Suppressed since this is Generated code
package com.apadmi.mockzilla.ui.ui.common.assets

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.DragCorner: ImageVector
    get() {
        if (_dragCorner != null) {
            return _dragCorner!!
        }
        _dragCorner = Builder(
            name = "DragCorner",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(21.71f, 11.29f)
                arcToRelative(0.996f, 0.996f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.41f, 0f)
                lineToRelative(-9f, 9f)
                arcToRelative(0.996f, 0.996f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.71f, 1.7f)
                curveToRelative(0.26f, 0f, 0.51f, -0.1f, 0.71f, -0.29f)
                lineToRelative(9f, -9f)
                arcToRelative(0.996f, 0.996f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -1.41f)
                close()
                moveToRelative(-0.9f, 4.91f)
                lineToRelative(-4.6f, 4.6f)
                arcToRelative(0.696f, 0.696f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.49f, 1.19f)
                horizontalLineToRelative(4.6f)
                curveToRelative(0.39f, 0f, 0.7f, -0.31f, 0.7f, -0.7f)
                verticalLineToRelative(-4.6f)
                curveToRelative(0f, -0.62f, -0.75f, -0.93f, -1.19f, -0.49f)
            }
        }.build()
        return _dragCorner!!
    }

private var _dragCorner: ImageVector? = null
