package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

private typealias HandlerCallback = (request: MockzillaHttpRequest, (MockzillaHttpResponse) -> Unit) -> Unit
private typealias MatcherCallback = (request: MockzillaHttpRequest, (Boolean) -> Unit) -> Unit

@Suppress("unused")  // Used from Swift
public fun setDefaultHandlerCallback(
    builder: EndpointConfiguration.Builder,
    block: HandlerCallback
) {
    builder.setDefaultHandler {
        suspendCancellableCoroutine { cont ->
            block(this) {
                cont.resume(it)
            }
        }
    }
}

@Suppress("unused")  // Used from Swift
public fun setErrorHandlerCallback(
    builder: EndpointConfiguration.Builder,
    block: HandlerCallback
) {
    builder.setErrorHandler {
        suspendCancellableCoroutine { cont ->
            block(this) {
                cont.resume(it)
            }
        }
    }
}

@Suppress("unused")  // Used from Swift
public fun setPatternMatcherCallback(
    builder: EndpointConfiguration.Builder,
    block: MatcherCallback
) {
    builder.setPatternMatcher {
        suspendCancellableCoroutine { cont ->
            block(this) {
                cont.resume(it)
            }
        }
    }
}
