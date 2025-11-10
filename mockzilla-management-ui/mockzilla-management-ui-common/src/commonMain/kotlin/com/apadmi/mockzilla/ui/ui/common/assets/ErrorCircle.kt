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

val Icons.ErrorCircle: ImageVector
    get() {
        if (_icErrorCircle != null) {
            return _icErrorCircle!!
        }
        _icErrorCircle = Builder(name = "IcErrorCircle", defaultWidth = 17.0.dp, defaultHeight =
            17.0.dp, viewportWidth = 17.0f, viewportHeight = 17.0f).apply {
            group {
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFB2C36)),
                    strokeLineWidth = 1.33333f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(8.1f, 14.767f)
                    curveTo(11.782f, 14.767f, 14.767f, 11.782f, 14.767f, 8.1f)
                    curveTo(14.767f, 4.418f, 11.782f, 1.433f, 8.1f, 1.433f)
                    curveTo(4.418f, 1.433f, 1.433f, 4.418f, 1.433f, 8.1f)
                    curveTo(1.433f, 11.782f, 4.418f, 14.767f, 8.1f, 14.767f)
                    close()
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFB2C36)),
                    strokeLineWidth = 1.33333f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(8.1f, 5.433f)
                    verticalLineTo(8.1f)
                }
                path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFFB2C36)),
                    strokeLineWidth = 1.33333f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(8.1f, 10.767f)
                    horizontalLineTo(8.107f)
                }
            }
        }
            .build()
        return _icErrorCircle!!
    }

private var _icErrorCircle: ImageVector? = null
