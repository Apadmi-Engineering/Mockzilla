@file:Suppress("MAGIC_NUMBER", "FLOAT_IN_ACCURATE_CALCULATIONS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private const val linearAnimationDuration = 800
private const val firstLineHeadDuration = 750
private const val firstLineTailDuration = 850
private const val firstLineHeadDelay = 0
private const val firstLineTailDelay = 333
internal val linearIndicatorWidth = 240.dp

internal val linearIndicatorHeight = 4.dp

private val firstLineHeadEasing = CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
private val firstLineTailEasing = CubicBezierEasing(0.4f, 0f, 1f, 1f)

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val offset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // if there isn't enough space to draw the stroke caps, fall back to StrokeCap.Butt
    if (strokeCap == StrokeCap.Butt || height > width) {
        // Progress line
        drawLine(color, Offset(barStart, offset), Offset(barEnd, offset), strokeWidth)
    } else {
        // need to adjust barStart and barEnd for the stroke caps
        val strokeCapOffset = strokeWidth / 2
        val coerceRange = strokeCapOffset..(width - strokeCapOffset)
        val adjustedBarStart = barStart.coerceIn(coerceRange)
        val adjustedBarEnd = barEnd.coerceIn(coerceRange)

        if (abs(endFraction - startFraction) > 0) {
            // Progress line
            drawLine(
                color,
                Offset(adjustedBarStart, offset),
                Offset(adjustedBarEnd, offset),
                strokeWidth,
                strokeCap,
            )
        }
    }
}

/**
 * A progress indicator that finishes its current pulse before disappearing after isLoading becomes false
 *
 * Based heavily on Comose LinearProgressIndicator
 *
 * @param isLoading Whether the indicator is active
 * @param modifier
 * @param color Pulse color
 * @param trackColor Color of the background
 * @param strokeCap
 * @param gapSize
 */
@Composable
internal fun TogglableProgressIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    gapSize: Dp = 4.dp
) {
    // Material spec timing constants
    val firstLineHead = rememberPausableAnimation(shouldContinue = isLoading, keyframes {
        durationMillis = linearAnimationDuration
        0f at firstLineHeadDelay using firstLineHeadEasing
        1f at firstLineHeadDuration + firstLineHeadDelay
    })

    val firstLineTail = rememberPausableAnimation(shouldContinue = isLoading, keyframes {
        durationMillis = linearAnimationDuration
        0f at firstLineTailDelay using firstLineTailEasing
        1f at firstLineTailDuration + firstLineTailDelay
    })

    Canvas(
        modifier.progressSemantics().size(linearIndicatorWidth, linearIndicatorHeight)
    ) {
        val strokeWidth = size.height
        val adjustedGapSize = if (strokeCap == StrokeCap.Butt || size.height > size.width) {
            gapSize
        } else {
            gapSize + strokeWidth.toDp()
        }
        val gapSizeFraction = adjustedGapSize / size.width.toDp()

        // Track before line 1
        if (firstLineHead.value < 1f - gapSizeFraction) {
            val start = if (firstLineHead.value > 0) firstLineHead.value + gapSizeFraction else 0f
            drawLinearIndicator(start, 1f, trackColor, strokeWidth, strokeCap)
        }

        // Line 1
        if (firstLineHead.value - firstLineTail.value > 0) {
            drawLinearIndicator(
                firstLineHead.value,
                firstLineTail.value,
                color,
                strokeWidth,
                strokeCap,
            )
        }

        if (firstLineTail.value > gapSizeFraction) {
            val start = 0f
            val end = if (firstLineTail.value < 1f) firstLineTail.value - gapSizeFraction else 1f
            drawLinearIndicator(start, end, trackColor, strokeWidth, strokeCap)
        }
    }
}

@Composable
private fun rememberPausableAnimation(
    shouldContinue: Boolean,
    animationSpec: AnimationSpec<Float>
): State<Float> {
    val anim = remember { Animatable(0f) }
    val isAnimating = remember { mutableStateOf(false) }

    LaunchedEffect(isAnimating.value) {
        if (shouldContinue && isAnimating.value) {
            anim.snapTo(0f)
            anim.animateTo(
                targetValue = 1f,
                animationSpec = animationSpec,
            )

            isAnimating.value = false
        }
    }

    LaunchedEffect(shouldContinue) {
        if (shouldContinue && !isAnimating.value) {
            isAnimating.value = true
        }
    }

    return anim.asState()
}
