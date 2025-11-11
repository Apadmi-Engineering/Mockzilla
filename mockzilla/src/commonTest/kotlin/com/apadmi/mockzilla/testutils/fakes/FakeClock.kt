package com.apadmi.mockzilla.testutils.fakes

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class FakeClock : Clock {
    var now: Instant = Clock.System.now()

    override fun now(): Instant = now
}
