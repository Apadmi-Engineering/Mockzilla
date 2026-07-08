package com.apadmi.mockzilla.lib.internal.msw

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import kotlin.js.Promise

@InternalMockzillaApi
@JsModule("msw/browser")
@JsNonModule
public external object MswBrowser {
    public fun setupWorker(vararg handlers: RestHandler): ServiceWorkerInstance
}

@InternalMockzillaApi
@JsModule("msw")
@JsNonModule
public external object Msw {
    public val http: Rest
}

@InternalMockzillaApi
public external object Rest {
    public fun all(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    public fun get(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    public fun post(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    public fun put(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    public fun patch(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    public fun delete(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler

    public fun options(
        path: String,
        resolver: (resolver: ResponseResolverInfo) -> Promise<Response>
    ): RestHandler
}

@InternalMockzillaApi
public external interface ResponseResolverInfo {
    public val request: Request
    public val requestId: String
}

@InternalMockzillaApi
public external interface DefaultContext {
    public fun status(status: Int): dynamic
    public fun json(body: Any): dynamic
    public fun text(body: String): dynamic
}

@InternalMockzillaApi
public external interface StartServiceWorkerOptions {
    public var onUnhandledRequest: String
}

@InternalMockzillaApi
public external interface ServiceWorkerInstance {
    public val context: ServiceWorkerContext
    public fun start(options: StartServiceWorkerOptions): Promise<Unit>
    public fun use(vararg handlers: RestHandler): Promise<Unit>
    public fun resetHandlers(): Promise<Unit>
    public fun stop(): Promise<Unit>
}

@InternalMockzillaApi
public external interface ServiceWorkerContext {
    public val isMockingEnabled: Boolean
}

@InternalMockzillaApi
public external interface RestHandler
