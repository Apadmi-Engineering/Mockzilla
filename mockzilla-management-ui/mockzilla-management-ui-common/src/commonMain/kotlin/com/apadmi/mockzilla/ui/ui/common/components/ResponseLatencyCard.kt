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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.theme.mockzillaMonoFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

private val maxLatencyMs = 1.days.inWholeMilliseconds.toInt()

@Suppress("MAGIC_NUMBER")
private val sliderMax = 60.seconds.inWholeMilliseconds.toFloat()

private fun Int.clamped() = min(max(0, this), maxLatencyMs)

@Composable
internal fun ResponseLatencyCard(
    modifier: Modifier = Modifier,
    initialValue: Int?,
    onChange: (Int) -> Unit,
    onReset: () -> Unit,
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // HEADER
        Text(
            text = "Response Latency",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // VALUE FIELD
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .background(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(6.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD1D5DB),
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = value?.let { "$it ms" } ?: "Not Set",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = mockzillaMonoFontFamily(),
                        color = if (value == null) Color(0xFF9CA3AF) else Color(0xFFEAB308),
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // MINUS BUTTON
            SmallSquareButton(onClick = { updateValue((value ?: 0) - 100) }) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null,
                    tint = Color(0xFF4B5563),
                    modifier = Modifier.size(20.dp),
                )
            }

            // PLUS BUTTON
            SmallSquareButton(onClick = { updateValue((value ?: 0) + 100) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFF4B5563),
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // SLIDER
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MockzillaSlider(
                value = value?.toFloat() ?: 0f,
                valueRange = 0f..sliderMax,
                modifier = Modifier.fillMaxWidth(),
                activeTrackColor = Color(0xFF00A896),
                onValueChange = {
                    updateValue(it.toInt())
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "0s",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = "60s",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp
                    )
                )
            }
        }

        // PRESET BUTTONS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(0, 300, 1000, 3000, 10000).forEach { ms ->
                val label = when {
                    ms == 0 -> "0 ms"
                    ms < 1000 -> "$ms ms"
                    else -> "${ms / 1000} s"
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF3F4F6))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFD1D5DB),
                            shape = RoundedCornerShape(4.dp)
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
                            color = Color(0xFF4B5563),
                            fontWeight = FontWeight.Medium,
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
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF3F4F6))
            .border(
                width = 1.dp,
                color = Color(0xFFD1D5DB),
                shape = RoundedCornerShape(6.dp)
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
