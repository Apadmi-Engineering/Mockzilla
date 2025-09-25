package com.apadmi.mockzilla.lib.sharedstate

import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import kotlinx.serialization.Serializable

/**
 * @property ip
 * @property port
 */
@Serializable
data class MockzillaSharedProcessState(val ip: String, val port: Int)

// Used to share state between `mockzilla` and `mockzilla-mobile-ui` when
// running on the same device
class MockzillaSharedProcessStateHandler(private val fileIo: FileIo) {
    private val fileName = "mockzilla-shared-state.json"
    private var sharedState: MockzillaSharedProcessState? = null
    suspend fun getSharedProcessState() = sharedState ?: fileIo.readFromCache(fileName)?.let {
        runCatching {
            JsonProvider.json.decodeFromString<MockzillaSharedProcessState>(it)
        }.getOrNull()
    }

    suspend fun setSharedProcessState(state: MockzillaSharedProcessState) {
        sharedState = state
        fileIo.saveToCache(
            fileName,
            JsonProvider.json.encodeToString<MockzillaSharedProcessState>(state)
        )
    }
}
