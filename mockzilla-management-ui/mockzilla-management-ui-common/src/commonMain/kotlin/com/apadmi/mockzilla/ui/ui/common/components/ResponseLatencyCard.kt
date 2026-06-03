package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.darkSurface
import com.apadmi.mockzilla.ui.ui.common.theme.mockzillaMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.warning

import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

private const val buttonCornerRadiusDark = 4
private const val buttonCornerRadiusLight = 6

private val maxLatencyMs = 1.days.inWholeMilliseconds.toInt()

@Suppress("MAGIC_NUMBER")
private val sliderMax = 60.seconds.inWholeMilliseconds.toFloat()

private fun Int.clamped() = min(max(0, this), maxLatencyMs)

@Suppress("MAGIC_NUMBER", "LONG_PARAMETER_LIST")
@Composable
internal fun ResponseLatencyCard(
    modifier: Modifier = Modifier,
    initialValue: Int?,
    onChange: (Int) -> Unit,
    onReset: () -> Unit,
    showHeader: Boolean = true,
    showBackground: Boolean = true,
    showBorder: Boolean = true,
    strings: Strings = LocalStrings.current,
) {
    var value by remember(initialValue) {
        mutableStateOf(initialValue)
    }

    val updateValue = remember(initialValue) {
        { it: Int ->
            val clamped = it.clamped()
            value = clamped
            onChange(clamped)
        }
    }

    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalForceDarkMode.current
    val cardShape = if (isDark) RoundedCornerShape(0.dp) else RoundedCornerShape(8.dp)
    val componentShape = if (isDark) RoundedCornerShape(4.dp) else RoundedCornerShape(6.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (showBackground) {
                    Modifier.background(
                        color = colorScheme.surfaceVariant,
                        shape = cardShape
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = colorScheme.outline,
                        shape = cardShape
                    )
                } else {
                    Modifier
                }
            )
            .padding(if (showBackground || showBorder) 16.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (showHeader) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.widgets.latency.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = onReset)
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = strings.widgets.latency.clear,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .background(
                        color = colorScheme.surfaceContainer,
                        shape = componentShape,
                    )
                    .border(
                        width = 1.dp,
                        color = colorScheme.outline,
                        shape = componentShape,
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = value?.let { strings.widgets.latency.millisecondLabel(it) } ?: strings.widgets.latency.notSet,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = mockzillaMonoFontFamily(),
                        color = value?.let {
                            colorScheme.warning.primary
                        } ?: colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SmallSquareButton(onClick = { updateValue((value ?: 0) - 100) }) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null,
                    tint = colorScheme.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            }

            SmallSquareButton(onClick = { updateValue((value ?: 0) + 100) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = colorScheme.onSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CustomSlider(
                value = value?.toFloat() ?: 0f,
                valueRange = 0f..sliderMax,
                modifier = Modifier.fillMaxWidth(),
                activeTrackColor = colorScheme.primary,
                onValueChange = {
                    updateValue(it.toInt())
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = strings.widgets.latency.sliderMin,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = strings.widgets.latency.sliderMax,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(0, 300, 1000, 3000, 10000).forEach { ms ->
                val isSelected = value == ms
                val label = when {
                    ms < 1000 -> strings.widgets.latency.millisecondLabel(ms)
                    else -> strings.widgets.latency.secondLabel(ms / 1000)
                }

                Box(
                    modifier = Modifier
                        .clip(componentShape)
                        .background(colorScheme.surfaceContainer)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) colorScheme.primary else colorScheme.outline,
                            shape = componentShape
                        )
                        .clickable {
                            updateValue(ms)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SmallSquareButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = colorScheme.surface == darkSurface
    val shape = if (isDark) {
        RoundedCornerShape(buttonCornerRadiusDark.dp)
    } else {
        RoundedCornerShape(buttonCornerRadiusLight.dp)
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(shape)
            .background(colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = colorScheme.outline,
                shape = shape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview
@Composable
private fun ResponseLatencyCardPreview() = PreviewSurface {
    ResponseLatencyCard(
        modifier = Modifier.padding(16.dp),
        initialValue = null,
        onChange = {},
        onReset = {},
    )
}
