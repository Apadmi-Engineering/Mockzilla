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

val Icons.InfoCircle: ImageVector
    get() {
        if (_icInfoCircle != null) {
            return _icInfoCircle!!
        }
        _icInfoCircle = Builder(name = "IcInfoCircle", defaultWidth = 16.0.dp, defaultHeight =
            16.0.dp, viewportWidth = 16.0f, viewportHeight = 16.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF004DFF)),
                    fillAlpha = 0.0f, strokeLineWidth = 1.33333f, strokeLineCap = Round,
                    strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(8.0f, 1.333f)
                    curveTo(4.318f, 1.333f, 1.333f, 4.318f, 1.333f, 8.0f)
                    curveTo(1.333f, 11.682f, 4.318f, 14.667f, 8.0f, 14.667f)
                    curveTo(11.682f, 14.667f, 14.667f, 11.682f, 14.667f, 8.0f)
                    curveTo(14.667f, 4.318f, 11.682f, 1.333f, 8.0f, 1.333f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF004DFF)),
                    fillAlpha = 0.0f, strokeLineWidth = 1.33333f, strokeLineCap = Round,
                    strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(8.0f, 10.667f)
                    verticalLineTo(8.0f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF004DFF)),
                    fillAlpha = 0.0f, strokeLineWidth = 1.33333f, strokeLineCap = Round,
                    strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(8.0f, 5.333f)
                    horizontalLineTo(7.993f)
                }
            }
        }
            .build()
        return _icInfoCircle!!
    }

private var _icInfoCircle: ImageVector? = null

