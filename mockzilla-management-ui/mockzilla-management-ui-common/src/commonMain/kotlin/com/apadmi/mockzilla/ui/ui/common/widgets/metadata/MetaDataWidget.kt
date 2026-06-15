package com.apadmi.mockzilla.ui.ui.common.widgets.metadata

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.components.DashedDivider
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SectionTitle
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import org.koin.core.parameter.parametersOf

private fun RunTarget.label(strings: Strings) = when (this) {
    RunTarget.AndroidDevice,
    RunTarget.AndroidEmulator -> strings.widgets.metaData.android

    RunTarget.IosDevice,
    RunTarget.IosSimulator -> strings.widgets.metaData.ios

    RunTarget.Jvm -> strings.widgets.metaData.jvm
    RunTarget.Js -> strings.widgets.metaData.js
}

@Composable
fun MetaDataWidget(
    device: Device,
    onClose: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    onGlobalControlsClick: (() -> Unit)? = null
) {
    val viewModel =
        getViewModel<MetaDataWidgetViewModel>(key = device.toString()) { parametersOf(device) }
    val state by viewModel.state.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    MetaDataWidgetContent(
        state = state,
        device = device,
        onClose = onClose,
        onRefresh = onRefresh,
        onGlobalControlsClick = onGlobalControlsClick,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded }
    )
}

@Composable
fun MetaDataRow(
    label: String,
    value: String,
    showDivider: Boolean = true
) = Column(modifier = Modifier.padding(horizontal = 4.dp)) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceMuted
        )
        Text(
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start,
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2
        )
    }
    if (showDivider) {
        DashedDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    }
}

// ── Row components ────────────────────────────────────────────────────────────

@Composable
fun SessionRow(label: String, value: String) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onSurfaceMuted
    )
    Text(
        text = value,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onSurface
    )
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Suppress("COMPLEX_EXPRESSION")
@Preview
@Composable
fun MetaDataListViewPreview() = PreviewSurface() {
    MetaDataWidgetContent(
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
            requestCount = 1284,
            uptime = "00:42:11",
            overridesCount = 1
        ),
        device = Device(ip = "127.0.0.1", port = "49812"),
        onClose = {},
        onRefresh = {},
        onGlobalControlsClick = {},
        isExpanded = false,
        onToggleExpand = {}
    )
}

@Composable
fun MetaDataWidgetContent(
    state: MetaDataWidgetViewModel.State,
    device: Device,
    onClose: (() -> Unit)?,
    onRefresh: (() -> Unit)?,
    onGlobalControlsClick: (() -> Unit)?,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    strings: Strings = LocalStrings.current
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        MetaDataHeader(
            state = state,
            onClose = onClose,
            onRefresh = onRefresh,
            onGlobalControlsClick = onGlobalControlsClick,
            isExpanded = isExpanded,
            onToggleExpand = onToggleExpand,
            strings = strings
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (state) {
                        is MetaDataWidgetViewModel.State.DisplayMetaData -> MetaDataListView(
                            state,
                            device,
                            strings
                        )

                        MetaDataWidgetViewModel.State.Error -> Text(
                            text = strings.widgets.metaData.error,
                            modifier = Modifier.padding(16.dp)
                        )

                        MetaDataWidgetViewModel.State.Loading -> Box(
                            Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetaDataListView(
    state: MetaDataWidgetViewModel.State.DisplayMetaData,
    device: Device? = null,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
) {
    AppSection(metaData = state.metaData, strings = strings)

    Spacer(modifier = Modifier.height(16.dp))

    DeviceSection(metaData = state.metaData, strings = strings)

    Spacer(modifier = Modifier.height(16.dp))

    SessionSection(
        uptime = state.uptime ?: "-",
        requests = state.requestCount?.toString() ?: "–",
        port = device?.port,
        overrides = state.overridesCount?.toString() ?: "-",
        strings = strings
    )
}

@Composable
fun MetaDataHeader(
    state: MetaDataWidgetViewModel.State,
    onClose: (() -> Unit)?,
    onRefresh: (() -> Unit)?,
    onGlobalControlsClick: (() -> Unit)?,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    strings: Strings
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mockzilla Logo and Title
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onToggleExpand),
            shape = RoundedCornerShape(8.dp),
            color = colorScheme.onSurface.copy(alpha = 0.05f),
            border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = Icons.MockzillaLogo,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = Color.Unspecified
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = strings.widgets.deviceConnection.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colorScheme.onSurfaceVariant
                        )
                    }

                    if (state is MetaDataWidgetViewModel.State.DisplayMetaData) {
                        Text(
                            text = strings.widgets.metaData.overrides(state.overridesCount ?: 0),
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Action Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            onGlobalControlsClick?.let {
                HeaderActionButton(icon = Icons.Default.DragIndicator, onClick = it)
            }
            onRefresh?.let {
                HeaderActionButton(icon = Icons.Default.Refresh, onClick = it)
            }
            onClose?.let {
                HeaderActionButton(icon = Icons.Default.Close, onClick = it)
            }
        }
    }
}

// ── Sections ─────────────────────────────────────────────────────────────────

@Composable
fun AppSection(metaData: MetaData, strings: Strings) = Column {
    SectionTitle(label = strings.widgets.metaData.appSection)
    MetaDataRow(strings.widgets.metaData.appName, metaData.appName)
    MetaDataRow(strings.widgets.metaData.appPackage, metaData.appPackage)
    MetaDataRow(strings.widgets.metaData.appVersion, metaData.appVersion)
    MetaDataRow(strings.widgets.metaData.mockzillaVersion, metaData.mockzillaVersion, showDivider = false)
}

@Composable
fun DeviceSection(metaData: MetaData, strings: Strings) = Column {
    SectionTitle(label = strings.widgets.metaData.deviceSection)
    MetaDataRow(strings.widgets.metaData.deviceModel, metaData.deviceModel)
    MetaDataRow(strings.widgets.metaData.operatingSystem, metaData.runTarget?.label(strings) ?: "-")
    MetaDataRow(strings.widgets.metaData.operatingSystemVersion, metaData.operatingSystemVersion, showDivider = false)
}

@Suppress("MAGIC_NUMBER")
@Composable
fun SessionSection(
    uptime: String,
    requests: String,
    port: String?,
    overrides: String,
    strings: Strings
) = Column {
    SectionTitle(label = strings.widgets.metaData.sessionSection)
    val cardShape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), cardShape)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SessionRow(label = strings.widgets.metaData.uptime, value = uptime)
            SessionRow(label = strings.widgets.metaData.requests, value = requests)
            SessionRow(label = strings.widgets.metaData.port, value = port?.let { ":$it" } ?: "–")
            SessionRow(label = strings.widgets.metaData.overridesLabel, value = overrides)
        }
    }
}

@Composable
private fun HeaderActionButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(10.dp),
        color = colorScheme.onSurface.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
