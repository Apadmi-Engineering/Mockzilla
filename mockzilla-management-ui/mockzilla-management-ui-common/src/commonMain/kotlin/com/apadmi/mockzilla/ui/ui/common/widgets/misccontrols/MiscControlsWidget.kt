package com.apadmi.mockzilla.ui.ui.common.widgets.misccontrols

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

@Composable
fun MiscControlsWidget(
    device: Device?
) {
    val viewModel = getViewModel<MiscControlsViewModel>(key = device?.toString()) { parametersOf(device) }
    MiscControlsWidgetContent(
        onRefreshAll = viewModel::refreshAllData,
        onClearAllOverrides = viewModel::clearAllOverrides
    )
}

@Composable
fun MiscControlsWidgetContent(
    onRefreshAll: () -> Unit,
    onClearAllOverrides: () -> Unit,
    strings: Strings = LocalStrings.current
) = Column {
    Button(onClick = onRefreshAll) {
        Text(strings.widgets.miscControls.refreshAll)
    }
    Button(onClick = onClearAllOverrides) {
        Text(strings.widgets.miscControls.clearOverrides)
    }
}

@Preview
@Composable
fun MiscControlsWidgetPreview() = PreviewSurface {
    MiscControlsWidgetContent(
        onRefreshAll = {},
        onClearAllOverrides = {}
    )
}
