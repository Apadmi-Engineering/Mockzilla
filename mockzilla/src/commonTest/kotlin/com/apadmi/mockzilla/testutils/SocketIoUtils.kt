package com.apadmi.mockzilla.testutils

import io.ktor.network.selector.SelectorManager
import kotlin.coroutines.CoroutineContext

internal expect class MockSelectorManager(
    context: CoroutineContext,
    whenBind: () -> Unit = {}
) : SelectorManager