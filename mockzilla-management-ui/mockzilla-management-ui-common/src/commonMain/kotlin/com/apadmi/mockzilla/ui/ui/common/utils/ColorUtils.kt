package com.apadmi.mockzilla.ui.ui.common.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.apadmi.mockzilla.ui.ui.common.theme.darkError
import com.apadmi.mockzilla.ui.ui.common.theme.darkMethodPatch
import com.apadmi.mockzilla.ui.ui.common.theme.darkOnSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.darkSuccess
import com.apadmi.mockzilla.ui.ui.common.theme.darkWarning
import com.apadmi.mockzilla.ui.ui.common.theme.methodDelete
import com.apadmi.mockzilla.ui.ui.common.theme.methodGet
import com.apadmi.mockzilla.ui.ui.common.theme.methodOther
import com.apadmi.mockzilla.ui.ui.common.theme.methodPatch
import com.apadmi.mockzilla.ui.ui.common.theme.methodPost
import com.apadmi.mockzilla.ui.ui.common.theme.methodPut
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning

import io.ktor.http.HttpStatusCode

@Suppress("MAGIC_NUMBER")
@Composable
public fun HttpStatusCode.color(): Color = when (this.value) {
    in 100..199 -> MaterialTheme.colorScheme.tertiary
    in 200..299 -> MaterialTheme.colorScheme.success.primary
    in 300..399 -> MaterialTheme.colorScheme.tertiary
    in 400..499 -> MaterialTheme.colorScheme.warning.primary
    in 500..599 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurfaceMuted
}

/** Non-composable variant for use in Canvas draw scopes. Uses dark-theme defaults.
 *
 * @return*/
@Suppress("MAGIC_NUMBER")
public fun HttpStatusCode.colorStatic(): Color = when (this.value) {
    in 100..199 -> darkMethodPatch
    in 200..299 -> darkSuccess
    in 300..399 -> darkMethodPatch
    in 400..499 -> darkWarning
    in 500..599 -> darkError
    else -> darkOnSurfaceMuted
}

@Suppress("MAGIC_NUMBER")
@Composable
public fun String.methodColor(): Color = when (this.uppercase()) {
    "GET" -> MaterialTheme.colorScheme.methodGet
    "POST" -> MaterialTheme.colorScheme.methodPost
    "PUT" -> MaterialTheme.colorScheme.methodPut
    "PATCH" -> MaterialTheme.colorScheme.methodPatch
    "DELETE" -> MaterialTheme.colorScheme.methodDelete
    else -> MaterialTheme.colorScheme.methodOther
}
