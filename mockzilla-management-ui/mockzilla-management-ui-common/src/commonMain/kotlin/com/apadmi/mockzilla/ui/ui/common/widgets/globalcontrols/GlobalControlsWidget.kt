package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.partialFailure
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.widgets.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsViewModel.*
import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.circle_check
import com.apadmi.mockzilla_management_ui_common.generated.resources.lightning_bolt
import com.apadmi.mockzilla_management_ui_common.generated.resources.play
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

@Composable
fun GlobalControlsWidget(device: Device) {
    val viewModel = getViewModel<GlobalControlsViewModel>(key = device.toString()) {
        parametersOf(device)
    }
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsState()

    GlobalControlsWidgetContent(
        state = state,
        onResetClicked = {
            focusManager.clearFocus()
            viewModel.resetAll()
        },
        onRestoreApiClicked = viewModel::restoreApi,
        onForceFailureClicked = viewModel::forceFailure,
        onLatencyChanged = viewModel::updateLatency,
        onResetLatency = {
            focusManager.clearFocus()
            viewModel.resetLatency()
        }
    )
}

@Composable
internal fun GlobalControlsWidgetContent(
    state: State,
    onResetClicked: () -> Unit,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    onLatencyChanged: (Int) -> Unit,
    onResetLatency: () -> Unit,
) = when (state) {
    is State.Idle -> GlobalControlsWidgetIdleContent(
        state,
        onResetClicked = onResetClicked,
        onRestoreApiClicked = onRestoreApiClicked,
        onForceFailureClicked = onForceFailureClicked,
        onLatencyChanged = onLatencyChanged,
        onResetLatency = onResetLatency
    )

    State.Loading -> Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun GlobalControlsWidgetIdleContent(
    state: State.Idle,
    strings: Strings = LocalStrings.current,
    onResetClicked: () -> Unit,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    onLatencyChanged: (Int) -> Unit,
    onResetLatency: () -> Unit,
) = Column(
    modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState()),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = strings.widgets.globalControls.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = strings.widgets.globalControls.subtitle,
                style = MaterialTheme.typography.titleSmall
            )
        }
        CustomOutlineButton(
            label = strings.widgets.globalControls.resetAllLabel,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            onClick = onResetClicked
        )
    }

    Box(Modifier.height(12.dp).fillMaxWidth().clipToBounds()) {
        TogglableProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            isLoading = state.isLoading,
            trackColor = Color.Transparent
        )
    }

    Column(
        Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GlobalFailureConfigBanner(
            state.apiFailureState,
            onRestoreApiClicked = onRestoreApiClicked,
            onForceFailureClicked = onForceFailureClicked
        )
        ResponseLatencyCard(
            strings = strings,
            onChange = onLatencyChanged,
            onReset = onResetLatency,
            initialValue = state.initialLatencyMs,
        )
    }
}

@Suppress("MAGIC_NUMBER")  // TODO: Remove
@Composable
private fun GlobalFailureConfigBanner(
    state: State.ApiFailureState,
    onRestoreApiClicked: () -> Unit,
    onForceFailureClicked: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val titleAndSubtitle = when (state) {
        State.ApiFailureState.FullFailure -> strings.widgets.globalControls.forcedFailureBannerConfig
        State.ApiFailureState.PartialFailure -> strings.widgets.globalControls.partialFailureBannerConfig
        State.ApiFailureState.Normal -> strings.widgets.globalControls.normalBehaviourBannerConfig
    }
    val borderAndTextColor = when (state) {
        State.ApiFailureState.FullFailure -> MaterialTheme.colorScheme.error
        State.ApiFailureState.PartialFailure -> MaterialTheme.colorScheme.partialFailure.primary
        State.ApiFailureState.Normal -> MaterialTheme.colorScheme.success.primary
    }
    val backgroundColorOverlay = when (state) {
        State.ApiFailureState.FullFailure -> MaterialTheme.colorScheme.errorContainer.copy(
            alpha = 0.1f
        )

        State.ApiFailureState.PartialFailure -> MaterialTheme.colorScheme.partialFailure.container.copy(
            alpha = 0.1f
        )

        State.ApiFailureState.Normal -> borderAndTextColor.copy(alpha = 0.1f)
    }

    val bannerIcon = when (state) {
        State.ApiFailureState.FullFailure,
        State.ApiFailureState.PartialFailure -> Res.drawable.lightning_bolt

        State.ApiFailureState.Normal -> Res.drawable.circle_check
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColorOverlay.compositeOver(
                    MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = borderAndTextColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(top = 20.dp, bottom = 12.dp, start = 16.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(resource = bannerIcon),
                contentDescription = null,
                tint = borderAndTextColor
            )
            Column {
                Text(
                    color = borderAndTextColor,
                    text = titleAndSubtitle.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    color = borderAndTextColor,
                    text = titleAndSubtitle.subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(2.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state != State.ApiFailureState.FullFailure) {
                // Force Fail Button
                CustomOutlineButton(
                    label = strings.widgets.globalControls.failButtonLabel,
                    borderColor = MaterialTheme.colorScheme.error,
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        contentColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.05f)
                            .compositeOver(MaterialTheme.colorScheme.surface)
                    ),
                    leadingIcon = painterResource(resource = Res.drawable.lightning_bolt),
                    contentPadding = PaddingValues(12.dp),
                    onClick = onForceFailureClicked
                )
            }

            if (state != State.ApiFailureState.Normal) {
                // Restore Api Button
                BaseButton(
                    label = strings.widgets.globalControls.restoreButtonLabel,
                    leadingIcon = painterResource(resource = Res.drawable.play),
                    backgroundColor = Color(0xFF_00_A6_3E),
                    contentColor = Color.White,
                    contentPadding = PaddingValues(12.dp),
                    onClick = onRestoreApiClicked
                )
            }
        }
    }
}

@Preview
@Composable
private fun GlobalControlsWidgetBannersPreview() = PreviewSurface {
    GlobalFailureConfigBannerPreview()
}


@Preview
@Composable
private fun GlobalControlsWidgetBannersDarkPreview() = PreviewSurface(darkTheme = true) {
    GlobalFailureConfigBannerPreview()
}

@Composable
private fun GlobalFailureConfigBannerPreview() {
    Column(
        modifier = Modifier.padding(8.dp).background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        State.ApiFailureState.entries.forEach {
            GlobalFailureConfigBanner(it, {}, {})
        }
    }
}

@Preview
@Composable
private fun GlobalControlsWidgetPreview() = PreviewSurface {
    GlobalControlsWidgetContent(
        State.Idle(10, isLoading = false, apiFailureState = State.ApiFailureState.FullFailure),
        onResetClicked = {},
        onRestoreApiClicked = {},
        onForceFailureClicked = {},
        onLatencyChanged = {},
        onResetLatency = {}
    )
}
