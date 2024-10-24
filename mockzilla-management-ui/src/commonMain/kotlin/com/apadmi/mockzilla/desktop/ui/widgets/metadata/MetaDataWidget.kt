package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings

import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel.*
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla_management_ui.generated.resources.Res
import com.apadmi.mockzilla_management_ui.generated.resources.mockzilla_logo

import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

private fun RunTarget.label(strings: Strings) = when (this) {
    RunTarget.AndroidDevice,
    RunTarget.AndroidEmulator -> strings.widgets.metaData.android

    RunTarget.IosDevice,
    RunTarget.IosSimulator -> strings.widgets.metaData.ios

    RunTarget.Jvm -> strings.widgets.metaData.jvm
}

@Composable
fun MetaDataWidget(device: Device) {
    val viewModel =
        getViewModel<MetaDataWidgetViewModel>(key = device.toString()) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    MetaDataWidgetContent(state)
}

@Composable
fun MetaDataWidgetContent(
    state: State,
    strings: Strings = LocalStrings.current
) = Box(
    Modifier.fillMaxSize().padding(4.dp),
    contentAlignment = Alignment.Center
) {
    Column {
        when (state) {
            is State.DisplayMetaData -> MetaDataListView(state.metaData, strings)
            State.Error -> Text("Error")
            State.Loading -> CircularProgressIndicator()
        }
    }
}

@Composable
fun MetaDataListView(
    metaData: MetaData,
    strings: Strings = LocalStrings.current
) = Column {
    Image(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = 16.dp)
            .height(50.dp),
        painter = painterResource(Res.drawable.mockzilla_logo),
        contentDescription = null
    )
    MetaDataRow(strings.widgets.metaData.appName, metaData.appName)
    MetaDataRow(strings.widgets.metaData.appPackage, metaData.appPackage)
    MetaDataRow(strings.widgets.metaData.operatingSystem, metaData.runTarget?.label(strings) ?: "-")
    MetaDataRow(strings.widgets.metaData.operatingSystemVersion, metaData.operatingSystemVersion)
    MetaDataRow(strings.widgets.metaData.deviceModel, metaData.deviceModel)
    MetaDataRow(strings.widgets.metaData.appVersion, metaData.appVersion)
    MetaDataRow(
        strings.widgets.metaData.mockzillaVersion,
        metaData.mockzillaVersion,
        showDivider = false
    )
}

@Composable
fun MetaDataRow(
    label: String,
    value: String,
    showDivider: Boolean = true
) = Box(modifier = Modifier.padding(4.dp)) {
    Row(
        modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(modifier = Modifier.weight(1f), textAlign = TextAlign.Start, text = label)
        Text(modifier = Modifier.weight(1f), textAlign = TextAlign.End, text = value)
    }
    if (showDivider) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}
