package com.apadmi.mockzilla.lib.internal.msw

import org.w3c.fetch.Request
import org.w3c.fetch.Response
import kotlin.js.Promise

@JsModule("msw/browser")
@JsNonModule
external object MswBrowser {
    fun setupWorker(vararg handlers: RestHandler): ServiceWorkerInstance
}

@JsModule("msw")
@JsNonModule
external object Msw {
    val http: Rest
}

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

external interface ResponseResolverInfo {
    val request: Request
    val requestId: String
}

external interface DefaultContext {
    fun status(status: Int): dynamic
    fun json(body: Any): dynamic
    fun text(body: String): dynamic
}

external interface ServiceWorkerInstance {
    val context: ServiceWorkerContext
    fun start(options: dynamic = definedExternally): Promise<Unit>
    fun use(vararg handlers: RestHandler): Promise<Unit>
    fun resetHandlers(): Promise<Unit>
    fun stop(): Promise<Unit>
}

external interface ServiceWorkerContext {
    val isMockingEnabled: Boolean
}

external interface RestHandler
