package com.apadmi.mockzilla.lib.internal.service

import kotlin.math.max
import kotlin.random.Random

internal interface DelayAndFailureDecision {
    fun shouldFail(
        failureProbability: Int,
    ): Boolean

    fun generateDelayMillis(delayMean: Int, delayVariance: Int): Long
}

internal object DelayAndFailureDecisionImpl : DelayAndFailureDecision {
    override fun shouldFail(
        failureProbability: Int,
    ): Boolean = Random.nextInt(0, 100) < failureProbability

    override fun generateDelayMillis(
        delayMean: Int,
        delayVariance: Int,
    ): Long {
        val rand = Random.nextDouble(0.0, 1.0)

        return max(
            0,
            (delayMean - delayVariance + rand * 2 * delayVariance).toLong()
        )
    }
}
