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

val Icons.CircleCheck: ImageVector
    get() {
        if (_circleCheck != null) {
            return _circleCheck!!
        }
        _circleCheck = Builder(name = "CircleCheck", defaultWidth = 20.0.dp, defaultHeight =
            20.0.dp, viewportWidth = 20.0f, viewportHeight = 20.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF00A63E)),
                    fillAlpha = 0.0f, strokeLineWidth = 1.66667f, strokeLineCap = Round,
                    strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(18.167f, 8.333f)
                    curveTo(18.548f, 10.201f, 18.277f, 12.143f, 17.399f, 13.835f)
                    curveTo(16.521f, 15.527f, 15.09f, 16.867f, 13.344f, 17.631f)
                    curveTo(11.597f, 18.396f, 9.642f, 18.538f, 7.803f, 18.035f)
                    curveTo(5.965f, 17.532f, 4.354f, 16.414f, 3.24f, 14.868f)
                    curveTo(2.126f, 13.321f, 1.576f, 11.439f, 1.681f, 9.536f)
                    curveTo(1.787f, 7.633f, 2.541f, 5.823f, 3.82f, 4.409f)
                    curveTo(5.098f, 2.995f, 6.822f, 2.062f, 8.705f, 1.765f)
                    curveTo(10.588f, 1.469f, 12.516f, 1.827f, 14.167f, 2.779f)
                }
                path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0xFF00A63E)),
                    fillAlpha = 0.0f, strokeLineWidth = 1.66667f, strokeLineCap = Round,
                    strokeLineJoin = StrokeJoin.Companion.Round, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                    moveTo(7.5f, 9.167f)
                    lineTo(10.0f, 11.667f)
                    lineTo(18.333f, 3.333f)
                }
            }
        }
            .build()
        return _circleCheck!!
    }

private var _circleCheck: ImageVector? = null
