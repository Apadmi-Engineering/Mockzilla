package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBanner
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.ResponseLatencyCard
import com.apadmi.mockzilla.ui.ui.common.components.SectionTitle
import com.apadmi.mockzilla.ui.ui.common.components.SurfaceHeader
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsViewModel.*

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
        },
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
        onResetLatency = onResetLatency,
    )

    State.Loading -> Box(
        modifier = Modifier.fillMaxSize().background(color = LocalMockzillaTokens.current.bg0),
    ) {
        CircularProgressIndicator(color = LocalMockzillaTokens.current.accent)
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
) {
    val tokens = LocalMockzillaTokens.current
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = tokens.bg0)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
    ) {
        Box {
            SurfaceHeader(
                title = strings.widgets.globalControls.title,
                subtitle = strings.widgets.globalControls.subtitle,
            ) {
                BaseButton(
                    label = strings.widgets.globalControls.resetAllLabel,
                    variant = ButtonVariant.Soft,
                    size = ButtonSize.Sm,
                    onClick = onResetClicked,
                )
            }

            Box(Modifier.height(12.dp).fillMaxWidth().clipToBounds()) {
                TogglableProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = state.isLoading,
                    trackColor = Color.Transparent,
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionTitle(label = strings.widgets.globalControls.title)
            ForceFailureBanner(
                state = state.apiFailureState,
                onRestoreApiClicked = onRestoreApiClicked,
                onForceFailureClicked = onForceFailureClicked,
            )
            ResponseLatencyCard(
                strings = strings,
                onChange = onLatencyChanged,
                onReset = onResetLatency,
                initialValue = state.initialLatencyMs,
            )
        }
    }
}

@Preview
@Composable
private fun GlobalControlsWidgetPreview() = PreviewSurface {
    GlobalControlsWidgetContent(
        State.Idle(10, isLoading = false, apiFailureState = ForceFailureBannerState.FullFailure),
        onResetClicked = {},
        onRestoreApiClicked = {},
        onForceFailureClicked = {},
        onLatencyChanged = {},
        onResetLatency = {},
    )
}

@Preview
@Composable
private fun GlobalControlsWidgetDarkPreview() = PreviewSurface(darkTheme = true) {
    GlobalControlsWidgetContent(
        State.Idle(0, isLoading = false, apiFailureState = ForceFailureBannerState.Normal),
        onResetClicked = {},
        onRestoreApiClicked = {},
        onForceFailureClicked = {},
        onLatencyChanged = {},
        onResetLatency = {},
    )
}
