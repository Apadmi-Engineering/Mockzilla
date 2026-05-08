package com.apadmi.mockzilla.ui.ui.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.MockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.dark_err
import com.apadmi.mockzilla.ui.ui.common.theme.dark_info
import com.apadmi.mockzilla.ui.ui.common.theme.dark_ok
import com.apadmi.mockzilla.ui.ui.common.theme.dark_warn

import io.ktor.http.HttpStatusCode

@Suppress("MAGIC_NUMBER")
@Composable
fun HttpStatusCode.color(): Color = colorFor(LocalMockzillaTokens.current)

@Suppress("MAGIC_NUMBER")
fun HttpStatusCode.colorFor(tokens: MockzillaTokens): Color = when (this.value) {
    in 100..199 -> tokens.info
    in 200..299 -> tokens.ok
    in 300..399 -> tokens.info
    in 400..499 -> tokens.warn
    in 500..599 -> tokens.err
    else -> tokens.fg2
}

/** Non-composable variant for use in Canvas draw scopes. Uses dark-theme defaults. */
@Suppress("MAGIC_NUMBER")
fun HttpStatusCode.colorStatic(): Color = when (this.value) {
    in 100..199 -> dark_info
    in 200..299 -> dark_ok
    in 300..399 -> dark_info
    in 400..499 -> dark_warn
    in 500..599 -> dark_err
    else -> Color.Gray
}

@Suppress("MAGIC_NUMBER")
fun String.methodColor(tokens: MockzillaTokens): Color = when (this.uppercase()) {
    "GET" -> tokens.methodGet
    "POST" -> tokens.methodPost
    "PUT" -> tokens.methodPut
    "PATCH" -> tokens.methodPatch
    "DELETE" -> tokens.methodDelete
    else -> tokens.methodOther
}
