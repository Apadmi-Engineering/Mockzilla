package com.apadmi.mockzilla.ui.engine

private const val maxLatencySliderSeconds = 60
private const val msPerSecond = 1000

internal const val maxLatencySliderMs = maxLatencySliderSeconds * msPerSecond

internal fun Int?.isOverflowingLatencySlider(): Boolean = (this ?: 0) > maxLatencySliderMs
