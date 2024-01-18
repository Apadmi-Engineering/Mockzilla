package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.service.Constants.errorPrefix
import com.apadmi.mockzilla.lib.internal.service.Constants.invalidEndpointIdChars
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig

internal object Constants {
    const val maxDelayMean = Int.MAX_VALUE / 2 - 1
    const val maxDelayVariance = Int.MAX_VALUE / 2 - 1
    const val errorPrefix = "Invalid Config:"
    const val invalidEndpointIdChars = "/\\:*"
}

@Throws(IllegalStateException::class)
internal fun MockzillaConfig.validate() {
    check(port >= 0) { "$errorPrefix Port cannot be negative" }
    check(endpoints.isNotEmpty()) { "$errorPrefix Config must contain at least 1 endpoint" }
    check(
        endpoints.distinctBy { it.key }.size == endpoints.size
    ) { "$errorPrefix Endpoints must have unique keys" }
    endpoints.forEach { it.validate() }
}

private fun EndpointConfiguration.validate() {
    check(key.isNotBlank()) {
        "$errorPrefix Endpoints must have non blank keys"
    }
    check(key.none { invalidEndpointIdChars.contains(it) }) {
        "Endpoint IDs cannot contain any of the following characters: $invalidEndpointIdChars"
    }
    check((0..Constants.maxDelayMean).contains(delay)) {
        "$errorPrefix Delay mean must be in range 0 to ${Int.MAX_VALUE / 2 - 1}"
    }
}
