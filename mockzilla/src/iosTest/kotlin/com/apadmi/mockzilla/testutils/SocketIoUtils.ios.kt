package com.apadmi.mockzilla.testutils

import io.ktor.network.selector.SelectInterest
import io.ktor.network.selector.Selectable
import io.ktor.network.selector.SelectorManager
import kotlin.coroutines.CoroutineContext

internal actual class MockSelectorManager actual constructor(
    private val context: CoroutineContext,
    val whenBind: () -> Unit
) : SelectorManager {
    override fun close() {
        /* Intentionally blank. */
    }

    override val coroutineContext: CoroutineContext
        get() = context

    override fun notifyClosed(selectable: Selectable) {
        /* Intentionally blank. */
    }

    override suspend fun select(selectable: Selectable, interest: SelectInterest) {
        whenBind()
    }
}