package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.DeviceRootViewModel.State.*
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomIconButton
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.warning

@Composable
private fun Connected.ErrorBannerState.backgroundColor() = when (this) {
    Connected.ErrorBannerState.ConnectionLost -> MaterialTheme.colorScheme.warning.container
    is Connected.ErrorBannerState.ApiError -> MaterialTheme.colorScheme.errorContainer
}

@InternalMockzillaApi
@Composable
public fun AnimatedErrorBanner(
    state: Connected.ErrorBannerState?,
    onRefreshAll: () -> Unit,
    onDismissError: () -> Unit,
): Unit = AnimatedContent(
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

@InternalMockzillaApi
@Composable
public fun ErrorBanner(
    state: Connected.ErrorBannerState,
    strings: Strings = LocalStrings.current,
    onRefreshAll: () -> Unit,
    onDismissError: () -> Unit,
) {
    var detailsExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.background(state.backgroundColor()).fillMaxWidth().statusBarsPadding(),
    ) {
        val borderColor = when (state) {
            is Connected.ErrorBannerState.ApiError -> MaterialTheme.colorScheme.error
            Connected.ErrorBannerState.ConnectionLost -> MaterialTheme.colorScheme.warning.primary
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 2.dp.value * density
                    val y = size.height - strokeWidth / 2

                    drawLine(
                        color = borderColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = when (state) {
                    is Connected.ErrorBannerState.ApiError -> strings.widgets.errorBanner.operationError(
                        state.operation
                    )

                    Connected.ErrorBannerState.ConnectionLost -> strings.widgets.errorBanner.connectionLost
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (state is Connected.ErrorBannerState.ApiError) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceMuted)) {
                            append(strings.widgets.errorBanner.apiErrorDescription)
                        }

                        state.status?.let {
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                append(" · ${it.value}")
                            }
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                Box(Modifier.weight(1f))
            }

            CustomButton(variant = ButtonVariant.Soft, onClick = {
                detailsExpanded = !detailsExpanded
            }, label = "Details")

            if (state is Connected.ErrorBannerState.ApiError) {
                CustomButton(
                    onClick = onRefreshAll,
                    label = strings.widgets.errorBanner.refreshButton,
                    variant = ButtonVariant.Solid
                )
            }

            CustomIconButton(
                onClick = onDismissError,
                imageVector = Icons.Default.Close,
                iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = LocalStrings.current.common.closeDescription,
                iconSize = 12.dp,
            )
        }

        AnimatedVisibility(
            visible = detailsExpanded
        ) {
            Box(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                ).fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .drawBehind {
                        val strokeWidth = 1.dp.value * density
                        val y = size.height - strokeWidth / 2

                        drawLine(
                            color = borderColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }.padding(12.dp)
            ) {
                when (state) {
                    is Connected.ErrorBannerState.ApiError -> ApiErrorDetails(state)
                    Connected.ErrorBannerState.ConnectionLost -> DisconnectedDetails()
                }
            }
        }
    }
}

@Composable
private fun ApiErrorDetails(
    state: Connected.ErrorBannerState.ApiError,
    strings: Strings.Widgets.ErrorBanner = LocalStrings.current.widgets.errorBanner
) = Text(
    text = buildAnnotatedString {
        state.status?.let {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(strings.statusLabel)
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                append(state.status.value.toString())
            }
            appendLine()
        }

        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(strings.messageLabel)
        }
        withStyle(
            SpanStyle(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceMuted
            )
        ) {
            append(state.rawError)
        }
    },
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.onSurface,
    fontFamily = LocalMonoFontFamily.current
)

@Composable
private fun DisconnectedDetails(
    strings: Strings.Widgets.ErrorBanner = LocalStrings.current.widgets.errorBanner
) = Text(
    text = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            appendLine(strings.connectionErrorTitle)
        }
        strings.connectionErrorTitlesAndBodies.forEachIndexed { index, (title, body) ->
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(title)
            }
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceMuted
                )
            ) {
                append(body)
            }
            if (index != strings.connectionErrorTitlesAndBodies.lastIndex) {
                appendLine()
            }
        }
    },
    style = MaterialTheme.typography.labelLarge,
    lineHeight = 20.sp,
    color = MaterialTheme.colorScheme.onSurface
)

@Preview
@Composable
private fun BannerPreview(useDark: Boolean = false) = PreviewSurface(useDark) {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ErrorBanner(
            state = Connected.ErrorBannerState.ConnectionLost,
            onRefreshAll = {},
            onDismissError = {}
        )

        ErrorBanner(
            state = Connected.ErrorBannerState.ConnectionLost,
            onRefreshAll = {},
            onDismissError = {}
        )
    }
}

@Preview
@Composable
private fun DarkBannerPreview() = BannerPreview(useDark = true)
