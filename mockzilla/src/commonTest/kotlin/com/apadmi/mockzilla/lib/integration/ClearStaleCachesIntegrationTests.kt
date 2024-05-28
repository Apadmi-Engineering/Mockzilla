package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigPatchRequestDto
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.lib.prepareMockzilla
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("TOO_LONG_FUNCTION")
class ClearStaleCachesIntegrationTests {
    private val fileIoForTesting = createFileIoforTesting()
    private fun prepareTestMockzilla(endpoints: List<EndpointConfiguration>) = prepareMockzilla(
        config = MockzillaConfig.Builder()
            .setPort(0).apply {
                endpoints.forEach { addEndpoint(it) }
            }.build(),
        metaData = MetaData(
            appName = "",
            appPackage = "",
            operatingSystemVersion = "",
            deviceModel = "",
            appVersion = "",
            runTarget = RunTarget.AndroidEmulator,
            mockzillaVersion = ""
        ), fileIo = fileIoForTesting, logger = Logger(StaticConfig()),
        zeroConfDiscoveryService = object : ZeroConfDiscoveryService {
            override fun makeDiscoverable(metaData: MetaData, port: Int) {
                /* No-Op */
            }
        }
    )

    @Test
    fun `startMockzilla - clears endpoints which are stale`() = runTest {
        /* Setup */
        val originalEndpoints = listOf(
            EndpointConfiguration.Builder("will not be stale").setVersionCode(0).build(),
            EndpointConfiguration.Builder("will be stale").setVersionCode(0).build(),
        )
        val di = prepareTestMockzilla(originalEndpoints)
        val params = startMockzilla(di.config, di, this)

        // Run a patch to populate the caches for both test endpoints
        HttpClient(CIO).patch(
            "${params.apiBaseUrl}/mock-data"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                Json.encodeToString(
                    SerializableEndpointConfigPatchRequestDto(
                        listOf(
                            SerializableEndpointPatchItemDto.allUnset("will not be stale").copy(
                                defaultBody = SetOrDont.Set("hello")
                            ),
                            SerializableEndpointPatchItemDto.allUnset("will be stale").copy(
                                defaultBody = SetOrDont.Set("bye")
                            )
                        )
                    )
                )
            )
        }
        // Won't be null if cache was set correctly
        check(di.localCacheService.getLocalCache(EndpointConfiguration.Key("will not be stale")) != null)
        check(di.localCacheService.getLocalCache(EndpointConfiguration.Key("will be stale")) != null)
        stopMockzilla()
        runCurrent()
        yield()

        /* Run Test */
        val newEndpoints = listOf(
            EndpointConfiguration.Builder("will not be stale").setVersionCode(0).build(),
            EndpointConfiguration.Builder("will be stale").setVersionCode(1).build(),
        )
        val di2 = prepareTestMockzilla(newEndpoints)
        startMockzilla(di2.config, di2, this)
        runCurrent()
        yield()

        /* Verify */
        assertNotNull(di2.localCacheService.getLocalCache(EndpointConfiguration.Key("will not be stale")))
        assertNull(di2.localCacheService.getLocalCache(EndpointConfiguration.Key("will be stale")))
    }
}
