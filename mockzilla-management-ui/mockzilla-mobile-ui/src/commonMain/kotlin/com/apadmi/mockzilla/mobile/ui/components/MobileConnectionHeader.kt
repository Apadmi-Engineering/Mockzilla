package com.apadmi.mockzilla.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily

@Suppress("MAGIC_NUMBER", "LONG_PARAMETER_LIST")
@Composable
internal fun MobileConnectionHeader(
    activeOverridesCount: Int,
    onRefresh: () -> Unit,
    onGlobalControlsClick: () -> Unit,
    onMetaDataClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val colorScheme = MaterialTheme.colorScheme
    val isDark = MaterialTheme.colorScheme.primary.luminance() < 0.5f // Simple dark check
    val accentColor = if (isDark) colorScheme.primary else Color(0xFF_0D9_488)

    Column(modifier = modifier.fillMaxWidth().background(colorScheme.surface)) {
        // Top section
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.surfaceContainer)
                        .border(1.dp, colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .clickable { onMetaDataClick() }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Logo box
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = colorScheme.surfaceContainer,
                        border = BorderStroke(1.dp, colorScheme.outlineVariant)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.MockzillaLogo,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = colorScheme.primary
                            )
                        }
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
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                        Text(
                            text = strings.widgets.metaData.overrides(activeOverridesCount),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontFamily = LocalMonoFontFamily.current
                            ),
                            color = if (activeOverridesCount > 0) accentColor else colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                // Global controls button
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = colorScheme.surface,
                    border = BorderStroke(1.dp, colorScheme.outlineVariant)
                ) {
                    IconButton(onClick = onGlobalControlsClick) {
                        Icon(
                            imageVector = Icons.Default.DragIndicator,
                            contentDescription = strings.widgets.globalControls.title,
                            modifier = Modifier.size(24.dp),
                            tint = colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Refresh button
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = colorScheme.surface,
                    border = BorderStroke(1.dp, colorScheme.outlineVariant)
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

        HorizontalDivider(color = colorScheme.outlineVariant)
    }
}

private fun Color.luminance(): Float {
    val r = red
    val g = green
    val b = blue
    return 0.2126f * r + 0.7152f * g + 0.0722f * b
}
