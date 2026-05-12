package com.apadmi.mockzilla.ui.ui.common.widgets.metadata

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SectionHeader
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

import kotlin.String

private fun RunTarget.label(strings: Strings) = when (this) {
    RunTarget.AndroidDevice,
    RunTarget.AndroidEmulator -> strings.widgets.metaData.android

    RunTarget.IosDevice,
    RunTarget.IosSimulator -> strings.widgets.metaData.ios

    RunTarget.Jvm -> strings.widgets.metaData.jvm
    RunTarget.Js -> strings.widgets.metaData.js
}

@Composable
fun MetaDataWidget(device: Device) {
    val viewModel =
        getViewModel<MetaDataWidgetViewModel>(key = device.toString()) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    MetaDataWidgetContent(state, device)
}

@Composable
fun MetaDataWidgetContent(
    state: MetaDataWidgetViewModel.State,
    device: Device? = null,
    strings: Strings = LocalStrings.current
) = Box(
    Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    when (state) {
        is MetaDataWidgetViewModel.State.DisplayMetaData -> MetaDataListView(state, device, strings)
        MetaDataWidgetViewModel.State.Error -> Text("Error")
        MetaDataWidgetViewModel.State.Loading -> CircularProgressIndicator()
    }
}

@Composable
fun MetaDataListView(
    state: MetaDataWidgetViewModel.State.DisplayMetaData,
    device: Device? = null,
    strings: Strings = LocalStrings.current
) = Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
    AppHeader(appName = state.metaData.appName, appPackage = state.metaData.appPackage)

    Spacer(modifier = Modifier.height(16.dp))

    DeviceSection(metaData = state.metaData, strings = strings)

    Spacer(modifier = Modifier.height(16.dp))

    SessionSection(
        uptime = state.uptime,
        requests = state.requestCount?.toString() ?: "–",
        port = device?.port,
    )
}

// ── Sections ─────────────────────────────────────────────────────────────────

@Composable
private fun DeviceSection(metaData: MetaData, strings: Strings) = Column {
    SectionHeader(title = strings.widgets.metaData.deviceSection)
    MetaDataRow(strings.widgets.metaData.operatingSystem, metaData.runTarget?.label(strings) ?: "-")
    MetaDataRow(strings.widgets.metaData.operatingSystemVersion, metaData.operatingSystemVersion)
    MetaDataRow(strings.widgets.metaData.deviceModel, metaData.deviceModel)
    MetaDataRow(strings.widgets.metaData.appVersion, metaData.appVersion)
    MetaDataRow(strings.widgets.metaData.mockzillaVersion, metaData.mockzillaVersion)
}

@Composable
private fun SessionSection(uptime: String, requests: String, port: String?) = Column {
    SectionHeader(title = "Session")
    val cardShape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), cardShape)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SessionRow(label = "uptime", value = uptime)
            SessionRow(label = "requests", value = requests)
            SessionRow(label = "port", value = port?.let { ":$it" } ?: "–")
        }
    }
}

// ── Row components ────────────────────────────────────────────────────────────

@Composable
private fun SessionRow(label: String, value: String) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
    Text(
        text = value,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun MetaDataRow(
    label: String,
    value: String,
    showDivider: Boolean = true
) = Column(modifier = Modifier.padding(horizontal = 4.dp)) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    if (showDivider) {
        DashedDivider()
    }
}

// ── App header ────────────────────────────────────────────────────────────────

@Composable
private fun AppHeader(appName: String, appPackage: String) = Row(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.MockzillaLogo,
            contentDescription = null
        )
    }
    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = appName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = appPackage,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            maxLines = 2
        )
    }
}

// ── Primitives ────────────────────────────────────────────────────────────────

@Composable
private fun DashedDivider() {
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.25f),
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview
@Composable
fun MetaDataListViewPreview() = PreviewSurface(darkTheme = true) {
    MetaDataListView(
        state = MetaDataWidgetViewModel.State.DisplayMetaData(
            metaData = MetaData(
                appName = "Runner",
                appPackage = "uk.co.homeserve.pega.sus.internal",
                operatingSystemVersion = "Version 18.5 (Build 22F77)",
                deviceModel = "iPhone 16 Plus",
                appVersion = "999.999.1",
                mockzillaVersion = "3.0.0-alpha2",
                runTarget = RunTarget.IosSimulator
            ),
            uptime = "5m 42s",
            requestCount = 17,
        ),
        device = Device(ip = "127.0.0.1", port = "49812")
    )
}
