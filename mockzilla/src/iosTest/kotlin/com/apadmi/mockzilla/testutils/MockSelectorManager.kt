package com.apadmi.mockzilla.testutils

import io.ktor.network.selector.SelectInterest
import io.ktor.network.selector.Selectable
import io.ktor.network.selector.SelectorManager
import kotlin.coroutines.CoroutineContext

/**
 * @property whenBind
 */
internal actual class MockSelectorManager actual constructor(
    private val context: CoroutineContext,
    val whenBind: () -> Unit
) : SelectorManager {
    override val coroutineContext: CoroutineContext
        get() = context
    override fun close() {
        /* Intentionally blank. */
    }

    override fun notifyClosed(selectable: Selectable) {
        /* Intentionally blank. */
    }

    override suspend fun select(selectable: Selectable, interest: SelectInterest) {
        whenBind()
    }
}
