package com.apadmi.mockzilla.desktop.ui.widgets.misccontrols

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import kotlin.reflect.KFunction0

@Composable
fun MiscControlsWidget() {
    val viewModel = getViewModel<MiscControlsViewModel>()
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