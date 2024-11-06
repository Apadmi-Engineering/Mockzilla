package com.apadmi.mockzilla.testutils

import io.ktor.network.selector.SelectInterest
import io.ktor.network.selector.Selectable
import io.ktor.network.selector.SelectorManager
import java.nio.channels.spi.SelectorProvider
import kotlin.coroutines.CoroutineContext

internal actual class MockSelectorManager actual constructor(
    val context: CoroutineContext,
    val whenBind: () -> Unit
) : SelectorManager {
    override val provider: SelectorProvider
        get() {
            whenBind()
            print("JVM provider requested")
            return SelectorProvider.provider()
        }

    override val coroutineContext: CoroutineContext
        get() {
            print("JVM coroutineContext requested")
            return context
        }

    override fun close() {
        /* Intentionally blank. */
    }

    override fun notifyClosed(selectable: Selectable) {
        /* Intentionally blank. */
    }

    override suspend fun select(selectable: Selectable, interest: SelectInterest) {
        print("JVM select executed")
        whenBind()
    }
}