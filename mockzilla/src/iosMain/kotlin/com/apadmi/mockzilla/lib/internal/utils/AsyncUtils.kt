package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Suppress("unused") // Used from Swift
fun setDefaultHandlerCallback(
    builder: EndpointConfiguration.Builder,
    block: (request: MockzillaHttpRequest, (MockzillaHttpResponse) -> Unit) -> Unit
) {
    builder.setDefaultHandler {
        suspendCancellableCoroutine { cont ->
            block(this) {
                cont.resume(it)
            }
        }
    }
}

@Suppress("unused") // Used from Swift
fun setErrorHandlerCallback(
    builder: EndpointConfiguration.Builder,
    block: (request: MockzillaHttpRequest, (MockzillaHttpResponse) -> Unit) -> Unit
) {
    builder.setErrorHandler {
        suspendCancellableCoroutine { cont ->
            block(this) {
                cont.resume(it)
            }
        }
    }
}

@Suppress("unused") // Used from Swift
fun setPatternMatcherCallback(
    builder: EndpointConfiguration.Builder,
    block: (request: MockzillaHttpRequest, (Boolean) -> Unit) -> Unit
) {
    builder.setPatternMatcher {
        suspendCancellableCoroutine { cont ->
            block(this) {
                cont.resume(it)
            }
        }
    }
}