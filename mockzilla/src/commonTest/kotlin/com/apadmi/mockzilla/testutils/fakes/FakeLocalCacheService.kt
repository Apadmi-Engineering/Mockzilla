package com.apadmi.mockzilla.testutils.fakes

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

class FakeLocalCacheService(
    private var getLocalCacheReturnValue: Map<EndpointConfiguration.Key, SerializableEndpointConfig?>? = null
) : LocalCacheService {
    override suspend fun getLocalCache(
        endpointKey: EndpointConfiguration.Key
    ) = getLocalCacheReturnValue?.get(endpointKey)

    var clearAllCachesCallCount = 0
    override suspend fun clearAllCaches() {
        clearAllCachesCallCount++
    }

    override suspend fun clearCache(keys: List<EndpointConfiguration.Key>) = Unit

    var patchLocalCachesArgument: Map<EndpointConfiguration, SerializableEndpointPatchItemDto>? = null
    override suspend fun patchLocalCaches(
        patches: Map<EndpointConfiguration, SerializableEndpointPatchItemDto>
    ) {
        patchLocalCachesArgument = patches
    }
}
