package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfiguration
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
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
        val result = sut.getLocalCache("I do not exist")

        /* Verify */
        assertNull(result)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `updateLocalCache and getLocalCache - returns value`() = runTest {
        /* Setup */
        val entryDummy = SerializableEndpointConfigurationPatchRequestDto.allUnset("id1", "").copy(
            headers = SetOrDont.Set(mapOf("my" to "header"))
        )
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        sut.updateLocalCache(entryDummy)
        val result = sut.getLocalCache("id1")

        /* Verify */
        assertEquals(SerializableEndpointConfiguration.allNulls("id1", "").copy(
            headers = mapOf("my" to "header")
        ), result)

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
        val result = runCatching { sut.getLocalCache("invalid") }

        /* Verify */
        assertTrue(result.exceptionOrNull() is IllegalStateException)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `updateLocalCache and getLocalCache - some overridden values - returns correctly`() = runTest {
        /* Setup */
        val initialCacheValue = SerializableEndpointConfigurationPatchRequestDto.allUnset("id1", "").copy(
            shouldFail = SetOrDont.Set(true),
            errorStatus = SetOrDont.Set(HttpStatusCode.BadGateway)
        )
        val cacheUpdate = SerializableEndpointConfigurationPatchRequestDto.allUnset("id1", "").copy(
            shouldFail = SetOrDont.Set(false),
            defaultStatus = SetOrDont.Set(HttpStatusCode.Created)
        )
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        sut.updateLocalCache(initialCacheValue)
        sut.updateLocalCache(cacheUpdate)
        val result = sut.getLocalCache("id1")

        /* Verify */
        assertEquals(SerializableEndpointConfiguration.allNulls("id1", "").copy(
            shouldFail = false,
            errorStatus = HttpStatusCode.BadGateway,
            defaultStatus = HttpStatusCode.Created
        ), result)

        /* Cleanup */
        sut.clearAllCaches()
    }
}
