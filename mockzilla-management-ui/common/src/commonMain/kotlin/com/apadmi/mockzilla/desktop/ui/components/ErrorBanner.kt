package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.AppRootViewModel
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.Icon

@Composable
fun AnimatedErrorBanner(
    state: AppRootViewModel.State.Connected.ErrorBannerState?,
    onRefreshAll: () -> Unit
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
    if (errorBannerState != null) {
        ErrorBanner(errorBannerState, onRefreshAll = onRefreshAll)
    }
}

@Composable
private fun ErrorBanner(
    state: AppRootViewModel.State.Connected.ErrorBannerState,
    strings: Strings = LocalStrings.current,
    onRefreshAll: () -> Unit
) = Box(
    modifier = Modifier.fillMaxWidth().background(state.backgroundColor())
) {
    Row(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = state.bannerText(strings),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = when (state) {
                AppRootViewModel.State.Connected.ErrorBannerState.ConnectionLost -> Color.Black
                AppRootViewModel.State.Connected.ErrorBannerState.UnknownError -> MaterialTheme.colorScheme.onErrorContainer
            }
        )
        if (state == AppRootViewModel.State.Connected.ErrorBannerState.UnknownError) {
            Button(onClick = onRefreshAll, contentPadding = PaddingValues(0.dp)) {
                Text(
                    strings.widgets.errorBanner.refreshButton,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun AppRootViewModel.State.Connected.ErrorBannerState.bannerText(strings: Strings): String = when (this) {
    AppRootViewModel.State.Connected.ErrorBannerState.ConnectionLost -> strings.widgets.errorBanner.connectionLost
    AppRootViewModel.State.Connected.ErrorBannerState.UnknownError -> strings.widgets.errorBanner.unknownError
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun AppRootViewModel.State.Connected.ErrorBannerState.backgroundColor() = when (this) {
    AppRootViewModel.State.Connected.ErrorBannerState.ConnectionLost -> Color(0XFFFFD129)
    AppRootViewModel.State.Connected.ErrorBannerState.UnknownError -> MaterialTheme.colorScheme.errorContainer
}

