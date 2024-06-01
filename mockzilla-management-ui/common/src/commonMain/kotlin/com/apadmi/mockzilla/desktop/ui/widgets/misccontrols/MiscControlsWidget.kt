package com.apadmi.mockzilla.desktop.ui.widgets.misccontrols

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KFunction0

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
    strings: Strings = LocalStrings.current) = Column {
    Button(onClick = onRefreshAll) {
        Text(strings.widgets.miscControls.refreshAll)
    }
    Button(onClick = onClearAllOverrides) {
        Text(strings.widgets.miscControls.clearOverrides)
    }
}