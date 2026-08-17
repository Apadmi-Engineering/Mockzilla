package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.testutils.dummy

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class LocalCacheServiceTests {
    @Test
    fun `getLocalCache - doesnt exist - returns null`() = runTest {
        /* Setup */
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        val result = sut.getLocalCache(EndpointConfiguration.Key("I do not exist"))

        /* Verify */
        assertNull(result)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `patchLocalCaches and getLocalCache - returns value`() = runTest {
        /* Setup */
        val dummyPreset = DashboardOverridePreset.dummy(headers = mapOf("my" to "header"))
        val entryDummy = SerializableEndpointPatchItemDto.allUnset("id1").copy(
            appliedPresetOverride = SetOrDont.Set(dummyPreset),
        )
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        sut.patchLocalCaches(mapOf(EndpointConfiguration.Builder("").build() to entryDummy))
        val result = sut.getLocalCache(entryDummy.key)

        /* Verify */
        assertEquals(
            SerializableEndpointConfig.allNulls("id1", "", Int.MIN_VALUE).copy(
                appliedPresetOverride = dummyPreset
            ), result
        )

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `getLocalCache - invalid - throws exception`() = runTest {
        /* Setup */
        val fileIo = createFileIoforTesting()
        val sut = LocalCacheServiceImpl(fileIo, Logger(StaticConfig()))

        /* Run Test */
        fileIo.saveToCache("invalid.json", "{,")
        val result = runCatching { sut.getLocalCache(EndpointConfiguration.Key("invalid")) }

        /* Verify */
        assertTrue(result.exceptionOrNull() is IllegalStateException)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `patchLocalCaches and getLocalCache - some overridden values - returns correctly`() =
        runTest {
            /* Setup */
            val initialCacheValue = SerializableEndpointPatchItemDto.allUnset("id1").copy(
                shouldFail = SetOrDont.Set(true),
                appliedPresetOverride = SetOrDont.Set(DashboardOverridePreset.dummy(HttpStatusCode.BadGateway))
            )
            val cacheUpdate = SerializableEndpointPatchItemDto.allUnset("id1").copy(
                shouldFail = SetOrDont.Set(false),
                delayMs = SetOrDont.Set(100),
                appliedPresetOverride = SetOrDont.Set(
                    DashboardOverridePreset.dummy(HttpStatusCode.BadGateway, body = "hello")
                )
            )
            val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

            /* Run Test */
            sut.patchLocalCaches(
                mapOf(EndpointConfiguration.Builder("").build() to initialCacheValue)
            )
            sut.patchLocalCaches(
                mapOf(
                    EndpointConfiguration.Builder("").setVersionCode(10).build() to cacheUpdate
                )
            )
            val result = sut.getLocalCache(initialCacheValue.key)

            /* Verify */
            assertEquals(
                SerializableEndpointConfig.allNulls("id1", "", 10).copy(
                    shouldFail = false,
                    delayMs = 100,
                    appliedPresetOverride = DashboardOverridePreset.dummy(HttpStatusCode.BadGateway, body = "hello")
                ), result
            )

            /* Cleanup */
            sut.clearAllCaches()
        }

    @Test
    fun `clearStaleCaches - clears caches where version code has changed`() = runTest {
        val endpoint1 = SerializableEndpointPatchItemDto.allUnset("id1").copy(
            shouldFail = SetOrDont.Set(true),
        )
        val endpoint2 = SerializableEndpointPatchItemDto.allUnset("id2").copy(
            shouldFail = SetOrDont.Set(true),
        )
        val endpoint3 = SerializableEndpointPatchItemDto.allUnset("id3").copy(
            shouldFail = SetOrDont.Set(true),
        )
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))
        sut.patchLocalCaches(
            mapOf(EndpointConfiguration.Builder("id1").setVersionCode(100).build() to endpoint1)
        )
        sut.patchLocalCaches(
            mapOf(EndpointConfiguration.Builder("id2").setVersionCode(50).build() to endpoint2)
        )
        sut.patchLocalCaches(
            mapOf(EndpointConfiguration.Builder("id3").setVersionCode(0).build() to endpoint3)
        )

        /* Run Test */
        sut.clearStaleCaches(
            listOf(
                EndpointConfiguration.Builder("id1").setVersionCode(50).build(),  // Different
                EndpointConfiguration.Builder("id2").setVersionCode(50).build(),  // Matches original
                EndpointConfiguration.Builder("id3").setVersionCode(50).build(),  // Different
            )
        )

        /* Verify */
        assertNull(sut.getLocalCache(EndpointConfiguration.Key("id1")))
        assertNotNull(sut.getLocalCache(EndpointConfiguration.Key("id2")))
        assertNull(sut.getLocalCache(EndpointConfiguration.Key("id3")))
    }
}
