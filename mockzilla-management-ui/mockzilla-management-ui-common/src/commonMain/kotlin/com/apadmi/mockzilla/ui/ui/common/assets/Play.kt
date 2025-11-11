@file:Suppress("diktat") // Suppressed since this is Generated code
package com.apadmi.mockzilla.ui.ui.common.assets

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Play: ImageVector
    get() {
        if (_play != null) {
            return _play!!
        }
        _play = Builder(name = "Play", defaultWidth = 16.0.dp, defaultHeight = 16.0.dp,
            viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFFffffff)),
                fillAlpha = 0.0f, strokeLineWidth = 1.33333f, strokeLineCap = Round,
                strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                pathFillType = NonZero) {
                moveTo(3.333f, 3.333f)
                curveTo(3.333f, 3.099f, 3.395f, 2.868f, 3.513f, 2.665f)
                curveTo(3.63f, 2.462f, 3.799f, 2.294f, 4.003f, 2.177f)
                curveTo(4.206f, 2.06f, 4.437f, 1.999f, 4.671f, 2.0f)
                curveTo(4.906f, 2.0f, 5.136f, 2.063f, 5.339f, 2.181f)
                lineTo(13.337f, 6.847f)
                curveTo(13.539f, 6.964f, 13.706f, 7.132f, 13.823f, 7.334f)
                curveTo(13.939f, 7.536f, 14.001f, 7.765f, 14.001f, 7.999f)
                curveTo(14.001f, 8.232f, 13.94f, 8.461f, 13.824f, 8.664f)
                curveTo(13.708f, 8.866f, 13.54f, 9.035f, 13.339f, 9.152f)
                lineTo(5.339f, 13.819f)
                curveTo(5.136f, 13.937f, 4.906f, 14.0f, 4.671f, 14.0f)
                curveTo(4.437f, 14.001f, 4.206f, 13.94f, 4.003f, 13.823f)
                curveTo(3.799f, 13.706f, 3.63f, 13.538f, 3.513f, 13.335f)
                curveTo(3.395f, 13.132f, 3.333f, 12.901f, 3.333f, 12.667f)
                verticalLineTo(3.333f)
                close()
            }
        }
            .build()
        return _play!!
    }

private var _play: ImageVector? = null

