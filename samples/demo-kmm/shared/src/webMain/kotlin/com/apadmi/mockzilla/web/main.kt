package com.apadmi.mockzilla.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.apadmi.mockzilla.launchManagementUi

import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.mock.Repository
import com.apadmi.mockzilla.mock.mockzillaConfig
import com.apadmi.mockzilla.mock.ui.App

import kotlin.js.ExperimentalWasmJsInterop
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)

fun main() {
    var baseUrl: String? = null
    GlobalScope.launch {
        baseUrl = startMockzilla("", "", mockzillaConfig).mockBaseUrl
    }
    ComposeViewport(viewportContainerId = "root") {
        App(Repository({ baseUrl!! })) {
            launchManagementUi()
        }
    }
}
