package com.apadmi.mockzilla

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.mobile.ui.MobileAppRoot
import com.apadmi.mockzilla.mobile.ui.utils.startMockzillaMobileUiKoin

import org.jetbrains.compose.web.renderComposable
import org.koin.dsl.module
import org.w3c.dom.HTMLElement

import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun launchManagementUi(rootId: String = "mockzilla-ui-root") {
    val div = document.getElementById(rootId) ?: createMockzillaUi(rootId)

    startMockzillaMobileUiKoin(module {
        single { FileIo() }
    })

    renderComposable(rootId) {
        ComposeViewport(viewportContainer = div, content = {
            MobileAppRoot {
                document.getElementById(rootId)?.remove()
            }
        })
    }
}

private fun createMockzillaUi(rootId: String): HTMLElement {
    val div = (document.createElement("div") as HTMLElement).apply {
        id = rootId
    }

    // Apply styles
    div.style.apply {
        position = "fixed"
        bottom = "0"
        left = "0"
        width = "90%"
        height = "90%"
        backgroundColor = "blue"
        zIndex = "9999"  // ensures it's on top
    }

    // Append to the document body
    document.body?.appendChild(div)
    return div
}
