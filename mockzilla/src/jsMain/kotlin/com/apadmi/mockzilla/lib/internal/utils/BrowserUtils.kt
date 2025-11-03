@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.browser.window

/**
 * @property name
 * @property version
 */
internal data class BrowserInfo(
    val name: String,
    val version: String
)

internal fun getBrowserInfo(): BrowserInfo {
    val userAgent = window.navigator.userAgent

    // Chrome (includes Chromium, Edge, Opera)
    val chromeMatch = Regex("Chrome/([0-9.]+)").find(userAgent)
    val edgeMatch = Regex("Edg/([0-9.]+)").find(userAgent)
    val firefoxMatch = Regex("Firefox/([0-9.]+)").find(userAgent)
    val safariMatch = Regex("Version/([0-9.]+).*Safari").find(userAgent)

    return when {
        edgeMatch != null -> BrowserInfo("Edge", edgeMatch.groupValues[1])
        chromeMatch != null -> BrowserInfo("Chrome", chromeMatch.groupValues[1])
        firefoxMatch != null -> BrowserInfo("Firefox", firefoxMatch.groupValues[1])
        safariMatch != null -> BrowserInfo("Safari", safariMatch.groupValues[1])
        else -> BrowserInfo("Unknown", "Unknown")
    }
}
