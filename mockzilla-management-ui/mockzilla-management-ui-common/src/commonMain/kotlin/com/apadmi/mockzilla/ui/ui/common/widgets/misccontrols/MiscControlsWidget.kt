package com.apadmi.mockzilla.ui.ui.common.widgets.misccontrols

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SectionHeader
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonContentAlignment
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalSetScaleFactor
import com.apadmi.mockzilla.ui.ui.common.theme.ScaleFactor
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

private data object PresentationModeScaleFactor {
    const val MIN = 0.8F
    const val MAX = 1.4F
    const val DEFAULT = 1.2F

    // These variables are in-memory caches for the state that we
    // may want to save to disk in the future. Without an in-memory
    // cache the compose state would reset on tab switches since the
    // entire tab tree UI is removed from the composition on tab switching
    // These are not themselves mutable state tracked by compose so we must
    // ensure we don't reference these values outside of the defaults
    // for setting up rememberSaveable states.
    var enabled = false
    var scaleFactor = DEFAULT
}

@Composable
fun MiscControlsWidget(
    device: Device?
) {
    val viewModel = getViewModel<MiscControlsViewModel>(key = device?.toString()) { parametersOf(device) }
    MiscControlsWidgetContent(
        onRefreshAll = viewModel::refreshAllData,
        onClearAllOverrides = viewModel::clearAllOverrides,
    )
}

@Composable
fun MiscControlsWidgetContent(
    onRefreshAll: () -> Unit,
    onClearAllOverrides: () -> Unit,
    strings: Strings = LocalStrings.current
) = Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
    SectionHeader(title = strings.widgets.miscControls.actionsSection)

    Spacer(modifier = Modifier.height(4.dp))

    BaseButton(
        modifier = Modifier.fillMaxWidth(),
        label = strings.widgets.miscControls.refreshAll,
        leadingIcon = Icons.Filled.Refresh,
        variant = ButtonVariant.Soft,
        size = ButtonSize.Lg,
        contentAlignment = ButtonContentAlignment.Start,
        onClick = onRefreshAll
    )

    Spacer(modifier = Modifier.height(2.dp))

    BaseButton(
        modifier = Modifier.fillMaxWidth(),
        label = strings.widgets.miscControls.clearOverrides,
        leadingIcon = Icons.Filled.Restore,
        variant = ButtonVariant.Soft,
        size = ButtonSize.Lg,
        contentAlignment = ButtonContentAlignment.Start,
        onClick = onClearAllOverrides
    )

    Spacer(modifier = Modifier.height(16.dp))

    var presentationMode by rememberSaveable { mutableStateOf(PresentationModeScaleFactor.enabled) }
    var presentationModeScaleFactor by rememberSaveable {
        mutableFloatStateOf(PresentationModeScaleFactor.scaleFactor)
    }
    val setScaleFactor = LocalSetScaleFactor.current
    PresentationModeSettings(
        presentationMode = presentationMode,
        onPresentationModeChange = { presentationModeEnabled ->
            presentationMode = presentationModeEnabled
            PresentationModeScaleFactor.enabled = presentationModeEnabled
            if (presentationModeEnabled) {
                setScaleFactor(presentationModeScaleFactor)
            } else {
                setScaleFactor(ScaleFactor.default)
            }
        },
        presentationModeScaleFactor = presentationModeScaleFactor,
        onPresentationModeScaleFactorChange = { scaleFactor ->
            setScaleFactor(scaleFactor)
            presentationModeScaleFactor = scaleFactor
            PresentationModeScaleFactor.scaleFactor = scaleFactor
        },
    )
}

@Composable
private fun PresentationModeSettings(
    presentationMode: Boolean,
    onPresentationModeChange: (Boolean) -> Unit,
    presentationModeScaleFactor: Float,
    onPresentationModeScaleFactorChange: (Float) -> Unit,
    strings: Strings = LocalStrings.current
) = Column {
    SectionHeader(title = strings.widgets.miscControls.presentationMode)

    Spacer(modifier = Modifier.height(4.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = presentationMode,
                onValueChange = { checked -> onPresentationModeChange(checked) },
                role = Role.Switch,
            )
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = strings.widgets.miscControls.presentationMode,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = presentationMode,
            onCheckedChange = null,
        )
    }

    AnimatedVisibility(visible = presentationMode) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        ) {
            Slider(
                value = presentationModeScaleFactor,
                onValueChange = { onPresentationModeScaleFactorChange(it) },
                steps = 5,
                valueRange = PresentationModeScaleFactor.MIN..PresentationModeScaleFactor.MAX,
            )
            Text(
                text = strings.widgets.miscControls.fontScaleLabel(presentationModeScaleFactor),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}


@Preview
@Composable
fun MiscControlsWidgetPreview() = PreviewSurface  {
    MiscControlsWidgetContent(
        onRefreshAll = {},
        onClearAllOverrides = {}
    )
}
