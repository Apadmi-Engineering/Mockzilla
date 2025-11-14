package com.apadmi.mockzilla.ui.ui.common.utils

import androidx.compose.ui.graphics.Color
import com.apadmi.mockzilla.ui.ui.common.theme.httpStatusCode_1xx
import com.apadmi.mockzilla.ui.ui.common.theme.httpStatusCode_2xx
import com.apadmi.mockzilla.ui.ui.common.theme.httpStatusCode_3xx
import com.apadmi.mockzilla.ui.ui.common.theme.httpStatusCode_4xx
import com.apadmi.mockzilla.ui.ui.common.theme.httpStatusCode_5xx
import io.ktor.http.HttpStatusCode

@Suppress("MAGIC_NUMBER")
fun HttpStatusCode.color() = when (this.value) {
    in 100..199 -> httpStatusCode_1xx
    in 200..299 -> httpStatusCode_2xx
    in 300..399 -> httpStatusCode_3xx
    in 400..499 -> httpStatusCode_4xx
    in 500..599 -> httpStatusCode_5xx
    else -> Color.Black
}
