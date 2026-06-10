package com.apadmi.mockzilla.mobile.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.ui.engine.device.StatefulDevice
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint

private fun RunTarget.label(strings: Strings) = when (this) {
    RunTarget.AndroidDevice,
    RunTarget.AndroidEmulator -> strings.widgets.metaData.android

    RunTarget.IosDevice,
    RunTarget.IosSimulator -> strings.widgets.metaData.ios

    RunTarget.Jvm -> strings.widgets.metaData.jvm
    RunTarget.Js -> strings.widgets.metaData.js
}

@Suppress("MAGIC_NUMBER")
@Composable
internal fun MobileConnectionHeader(
    statefulDevice: StatefulDevice,
    requestCount: Int,
    activeOverridesCount: Int,
    uptime: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val strings = LocalStrings.current
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth()) {
        // Top section with light background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surfaceContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Main box
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.background)
                        .clickable { isExpanded = !isExpanded }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Logo box
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colorScheme.background)
                            .border(1.dp, colorScheme.outline, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = Icons.MockzillaLogo,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = strings.widgets.deviceConnection.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = colorScheme.onSurface
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = colorScheme.onSurfaceFaint
                            )
                        }
                        Text(
                            text = strings.widgets.metaData.overrides(activeOverridesCount),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            ),
                            color = if (activeOverridesCount > 0) colorScheme.primary else colorScheme.onSurfaceFaint
                        )
                    }
                }

                // Refresh button
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, colorScheme.outline)
                ) {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = strings.widgets.errorBanner.refreshButton,
                            modifier = Modifier.size(24.dp),
                            tint = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = colorScheme.outline)

        // Expanded section with white background
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                InfoSection(
                    title = strings.widgets.metaData.appSection,
                    rows = listOf(
                        strings.widgets.metaData.appName to statefulDevice.metaData.appName,
                        strings.widgets.metaData.appPackage to statefulDevice.metaData.appPackage,
                        strings.widgets.metaData.appVersion to statefulDevice.metaData.appVersion,
                        strings.widgets.metaData.mockzillaVersion to statefulDevice.metaData.mockzillaVersion
                    )
                )

                Spacer(Modifier.height(20.dp))

                InfoSection(
                    title = strings.widgets.metaData.deviceSection,
                    rows = listOf(
                        strings.widgets.metaData.deviceModel to statefulDevice.metaData.deviceModel,
                        strings.widgets.metaData.operatingSystem to (statefulDevice.metaData.runTarget?.label(strings) ?: "-"),
                        strings.widgets.metaData.operatingSystemVersion to statefulDevice.metaData.operatingSystemVersion
                    )
                )

                Spacer(Modifier.height(20.dp))

                InfoSection(title = strings.widgets.metaData.sessionSection) {
                    SessionCard(
                        uptime = uptime,
                        requests = requestCount.toString(),
                        port = statefulDevice.device.port,
                        overrides = activeOverridesCount.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    rows: List<Pair<String, String>>
) {
    Column {
        SectionHeader(title)
        rows.forEach { (label, value) ->
            InfoRow(label, value)
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        SectionHeader(title)
        content()
    }
}

@Composable
private fun SectionHeader(title: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            ),
            color = colorScheme.onSurfaceFaint
        )
        Spacer(Modifier.width(8.dp))
        HorizontalDivider(modifier = Modifier.weight(1f), color = colorScheme.outline.copy(alpha = 0.5f))
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(0.35f),
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceFaint
            )
            Text(
                text = value,
                modifier = Modifier.weight(0.65f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                color = colorScheme.onSurface,
                textAlign = TextAlign.Start
            )
        }
        DashedDivider()
    }
}

@Composable
private fun DashedDivider(
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
) {
    Canvas(
        modifier.height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()), 0f),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun SessionCard(
    uptime: String,
    requests: String,
    port: String,
    overrides: String
) {
    val strings = LocalStrings.current
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        color = colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SessionRow(strings.widgets.metaData.uptime, uptime)
            SessionRow(strings.widgets.metaData.requests, requests)
            SessionRow(strings.widgets.metaData.port, ":$port")
            SessionRow(strings.widgets.metaData.overridesLabel, overrides)
        }
    }
}

@Composable
private fun SessionRow(label: String, value: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            ),
            color = colorScheme.onSurfaceFaint
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            ),
            color = colorScheme.onSurface
        )
    }
}
