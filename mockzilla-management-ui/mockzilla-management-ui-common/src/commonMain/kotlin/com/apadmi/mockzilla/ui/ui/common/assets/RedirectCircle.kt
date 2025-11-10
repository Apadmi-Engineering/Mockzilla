@file:Suppress("diktat") // Suppressed since this is Generated code
package com.apadmi.mockzilla.ui.ui.common.assets

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.RedirectCircle: ImageVector
    get() {
        if (_icRedirectCircle != null) {
            return _icRedirectCircle!!
        }
        _icRedirectCircle = Builder(name = "IcRedirectCircle", defaultWidth = 17.0.dp, defaultHeight
        = 17.0.dp, viewportWidth = 17.0f, viewportHeight = 17.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFFA69011)),
                    fillAlpha = 0.0f, strokeLineWidth = 1.33333f, strokeLineCap = Round,
                    strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(8.1f, 14.767f)
                    curveTo(11.782f, 14.767f, 14.767f, 11.782f, 14.767f, 8.1f)
                    curveTo(14.767f, 4.418f, 11.782f, 1.433f, 8.1f, 1.433f)
                    curveTo(4.418f, 1.433f, 1.433f, 4.418f, 1.433f, 8.1f)
                    curveTo(1.433f, 11.782f, 4.418f, 14.767f, 8.1f, 14.767f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFA69011)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(5.5f, 12.1f)
                    lineTo(3.0f, 9.6f)
                    lineTo(5.5f, 7.1f)
                    lineTo(6.2f, 7.813f)
                    lineTo(4.912f, 9.1f)
                    horizontalLineTo(8.5f)
                    verticalLineTo(10.1f)
                    horizontalLineTo(4.912f)
                    lineTo(6.2f, 11.388f)
                    lineTo(5.5f, 12.1f)
                    close()
                    moveTo(10.5f, 9.1f)
                    lineTo(9.8f, 8.388f)
                    lineTo(11.087f, 7.1f)
                    horizontalLineTo(7.5f)
                    verticalLineTo(6.1f)
                    horizontalLineTo(11.087f)
                    lineTo(9.8f, 4.813f)
                    lineTo(10.5f, 4.1f)
                    lineTo(13.0f, 6.6f)
                    lineTo(10.5f, 9.1f)
                    close()
                }
            }
        }
            .build()
        return _icRedirectCircle!!
    }

private var _icRedirectCircle: ImageVector? = null

