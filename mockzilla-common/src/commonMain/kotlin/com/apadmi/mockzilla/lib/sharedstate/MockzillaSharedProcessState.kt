package com.apadmi.mockzilla.lib.sharedstate

import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import kotlinx.serialization.Serializable

/**
 * The IP address and port of a running Mockzilla server, persisted so it can be shared between
 * processes on the same device. Written by the mockzilla server and read by the management UI module.
 *
 * @property ip The IP address the server is listening on.
 * @property port The port the server is bound to.
 */
@Serializable
data class MockzillaSharedProcessState(val ip: String, val port: Int)

/**
 * Reads and writes [MockzillaSharedProcessState] to a file cache, allowing the Mockzilla server
 * and the management UI to exchange connection details when running on the same device.
 */
class MockzillaSharedProcessStateHandler(private val fileIo: FileIo) {
    private val fileName = "mockzilla-shared-state.json"
    private var sharedState: MockzillaSharedProcessState? = null

    /**
     * Returns the most recently written [MockzillaSharedProcessState], reading from the file cache
     * if no value has been set in the current process. Returns `null` if no state has been
     * persisted yet.
     *
     * @return The shared process state, or `null` if unavailable.
     */
    suspend fun getSharedProcessState() = sharedState ?: fileIo.readFromCache(fileName)?.let {
        runCatching {
            JsonProvider.json.decodeFromString<MockzillaSharedProcessState>(it)
        }.getOrNull()
    }

    /**
     * Writes [state] to both the in-memory cache and the file cache so it is available to other
     * processes reading via [getSharedProcessState].
     *
     * @param state The server connection details to persist.
     */
    suspend fun setSharedProcessState(state: MockzillaSharedProcessState) {
        sharedState = state
        fileIo.saveToCache(
            fileName,
            JsonProvider.json.encodeToString<MockzillaSharedProcessState>(state)
        )
    }
}
