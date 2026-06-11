package com.apadmi.mockzilla.lib.internal.msw

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import kotlin.js.Promise

@InternalMockzillaApi
@JsModule("msw/browser")
@JsNonModule
external object MswBrowser {
    fun setupWorker(vararg handlers: RestHandler): ServiceWorkerInstance
}

@InternalMockzillaApi
@JsModule("msw")
@JsNonModule
external object Msw {
    val http: Rest
}

@InternalMockzillaApi
external object Rest {
    fun all(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    fun get(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    fun post(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    fun put(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    fun patch(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    fun delete(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    fun options(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler
}

@InternalMockzillaApi
external interface ResponseResolverInfo {
    val request: Request
    val requestId: String
}

@InternalMockzillaApi
external interface DefaultContext {
    fun status(status: Int): dynamic
    fun json(body: Any): dynamic
    fun text(body: String): dynamic
}

@InternalMockzillaApi
external interface StartServiceWorkerOptions {
    var onUnhandledRequest: String
}

@InternalMockzillaApi
external interface ServiceWorkerInstance {
    val context: ServiceWorkerContext
    fun start(options: StartServiceWorkerOptions): Promise<Unit>
    fun use(vararg handlers: RestHandler): Promise<Unit>
    fun resetHandlers(): Promise<Unit>
    fun stop(): Promise<Unit>
}

@InternalMockzillaApi
external interface ServiceWorkerContext {
    val isMockingEnabled: Boolean
}

@InternalMockzillaApi
external interface RestHandler
