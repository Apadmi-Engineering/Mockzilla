package com.apadmi.mockzilla.testutils

import io.ktor.network.selector.SelectInterest
import io.ktor.network.selector.Selectable
import io.ktor.network.selector.SelectorManager
import java.nio.channels.spi.SelectorProvider
import kotlin.coroutines.CoroutineContext

internal actual class MockSelectorManager actual constructor(
    context: CoroutineContext,
    whenBind: () -> Unit
) : SelectorManager {
    override val provider: SelectorProvider
        get() = throw NotImplementedError()

    override val coroutineContext: CoroutineContext
        get() = throw NotImplementedError()

    override fun close() {
        throw NotImplementedError()
    }

    override fun notifyClosed(selectable: Selectable) {
        throw NotImplementedError()
    }

    override suspend fun select(selectable: Selectable, interest: SelectInterest) {
        throw NotImplementedError()
    }
}