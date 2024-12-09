package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.AppRootViewModel.State.*
import com.apadmi.mockzilla.desktop.ui.theme.theme_warning_background

private fun Connected.ErrorBannerState.bannerText(strings: Strings): String =
    when (this) {
        Connected.ErrorBannerState.ConnectionLost -> strings.widgets.errorBanner.connectionLost
        Connected.ErrorBannerState.UnknownError -> strings.widgets.errorBanner.unknownError
    }

@Suppress("MAGIC_NUMBER")
@Composable
private fun Connected.ErrorBannerState.backgroundColor() = when (this) {
    Connected.ErrorBannerState.ConnectionLost -> theme_warning_background
    Connected.ErrorBannerState.UnknownError -> MaterialTheme.colorScheme.errorContainer
}

@Composable
fun AnimatedErrorBanner(
    state: Connected.ErrorBannerState?,
    onRefreshAll: () -> Unit,
    onDismissError: () -> Unit,
) = AnimatedContent(
    targetState = state,
    transitionSpec = {
        when {
            targetState == null -> {
                // Animate up when going to null
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            }

            initialState == null -> {
                // Animate down when coming from null
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }

            else -> fadeIn() togetherWith fadeOut()
        }.using(SizeTransform(clip = false))
    }
) { errorBannerState ->
    errorBannerState?.let {
        ErrorBanner(errorBannerState, onRefreshAll = onRefreshAll, onDismissError = onDismissError)
    }
}

@Composable
private fun ErrorBanner(
    state: Connected.ErrorBannerState,
    strings: Strings = LocalStrings.current,
    onRefreshAll: () -> Unit,
    onDismissError: () -> Unit,
) = Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.CenterEnd,
) {
    Row(
        modifier = Modifier.padding(16.dp)
            .clip(RoundedCornerShape(25))
            .background(state.backgroundColor())
            .padding(12.dp).pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    onDismissError()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val onColor = when (state) {
            Connected.ErrorBannerState.ConnectionLost -> Color.Black
            Connected.ErrorBannerState.UnknownError -> MaterialTheme.colorScheme.onErrorContainer
        }
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            tint = onColor
        )

        Text(
            text = state.bannerText(strings),
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 0.sp),
            color = onColor
        )

        if (state == Connected.ErrorBannerState.UnknownError) {
            Button(onClick = onRefreshAll, contentPadding = PaddingValues(0.dp)) {
                Text(
                    strings.widgets.errorBanner.refreshButton,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
