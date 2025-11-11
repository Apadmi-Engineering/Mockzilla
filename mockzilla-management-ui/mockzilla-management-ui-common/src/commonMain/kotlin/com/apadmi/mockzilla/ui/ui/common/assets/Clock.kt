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
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


val Icons.Clock: ImageVector
    get() {
        if (_clock != null) {
            return _clock!!
        }
        _clock = Builder(name = "Clock", defaultWidth = 20.0.dp, defaultHeight = 20.0.dp,
            viewportWidth = 20.0f, viewportHeight = 20.0f).apply {
            group {
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF0A0A0A)),
                    strokeLineWidth = 1.667f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(10.0f, 5.0f)
                    verticalLineToRelative(5.0f)
                    lineToRelative(3.333f, 1.667f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF0A0A0A)),
                    strokeLineWidth = 1.667f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(10.0f, 18.333f)
                    arcTo(8.333f, 8.333f, 0.0f, true, false, 1.666f, 10.0f)
                    arcTo(8.333f, 8.333f, 0.0f, false, false, 10.0f, 18.333f)
                    close()
                }
            }
        }
            .build()
        return _clock!!
    }

private var _clock: ImageVector? = null

