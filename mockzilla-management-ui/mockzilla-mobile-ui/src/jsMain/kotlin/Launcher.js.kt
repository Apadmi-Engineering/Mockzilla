package com.apadmi.mockzilla

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.mobile.ui.MobileAppRoot
import com.apadmi.mockzilla.mobile.ui.utils.startMockzillaMobileUiKoin

import org.jetbrains.compose.web.renderComposable
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.dsl.module
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement

import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class, ExperimentalJsExport::class)
@JsExport
fun launchManagementUi(rootId: String = "mockzilla-ui-root") {
    val div = document.getElementById(rootId) ?: createMockzillaUi(rootId)

    startMockzillaMobileUiKoin(module {
        single { FileIo() }
    })

    onWasmReady {
        renderComposable(rootId) {
            ComposeViewport(viewportContainer = div, content = {
                MobileAppRoot {
                    document.getElementById(rootId)?.remove()
                }
            })
        }
    }
}

private fun createMockzillaUi(rootId: String): HTMLElement {
    // Inject responsive width rule via media query
    val styleEl = (document.createElement("style") as HTMLStyleElement).apply {
        textContent = """
            #$rootId {
                width: 600px;
            }
            @media (max-width: 1024px) {
                #$rootId {
                    width: 100% !important;
                }
            }
        """.trimIndent()
    }
    document.head?.appendChild(styleEl)

    val div = (document.createElement("div") as HTMLElement).apply {
        id = rootId
    }

    // Apply styles
    div.style.apply {
        position = "fixed"
        bottom = "0"
        left = "0"
        height = "90%"
        zIndex = "9999"  // ensures it's on top
        borderTopLeftRadius = "16px"
        borderTopRightRadius = "16px"
        boxShadow = "4px -4px 16px rgba(0, 0, 0, 0.4)"
        overflowX = "hidden"
        overflowY = "hidden"
    }

    // Append to the document body
    document.body?.appendChild(div)
    return div
}
