package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.Clock
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

private val maxLatencyMs = 1.days.inWholeMilliseconds.toInt()

@Suppress("MAGIC_NUMBER")
private val sliderMax = 60.seconds.inWholeMilliseconds.toFloat()
private fun Int.clamped() = min(max(0, this), maxLatencyMs)

private val cardShape = RoundedCornerShape(10.dp)

@Composable
internal fun ResponseLatencyCard(
    modifier: Modifier = Modifier,
    initialValue: Int?,
    onChange: (Int) -> Unit,
    onReset: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val tokens = LocalMockzillaTokens.current
    var value by remember(initialValue) { mutableStateOf(initialValue) }
    val updateValue = remember(initialValue) {
        { it: Int ->
            value = it.clamped()
            onChange(it.clamped())
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(color = tokens.bg2)
            .border(width = 1.dp, color = tokens.line1, shape = cardShape)
            .padding(start = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        SectionTitle(label = strings.widgets.latency.title)

        Row(
            modifier = Modifier.padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SquareIconButton(
                enabled = value != null && value != 0,
                onClick = { updateValue((value ?: 0) - 100) },
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus")
            }
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                modifier = Modifier.weight(1f),
                value = value?.toString() ?: "",
                placeholder = { Text("Not Set") },
                onValueChange = { updateValue(it.toIntOrNull() ?: 0) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = {
                    value?.let {
                        Text(strings.widgets.latency.millisecondLabel)
                    } ?: Box(Modifier)
                },
            )
            Spacer(Modifier.size(10.dp))
            SquareIconButton(
                enabled = value != null && value != maxLatencyMs,
                onClick = { updateValue((value ?: 0) + 100) },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
            }
            Spacer(Modifier.size(6.dp))
            IconButton(onClick = onReset, enabled = value != null) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.Restore,
                    contentDescription = strings.common.resetDescription,
                    tint = tokens.fg2,
                )
            }
        }

        Row(
            modifier = Modifier.padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = strings.widgets.latency.sliderMin,
                style = MaterialTheme.typography.bodySmall,
                color = tokens.fg2,
            )
            MockzillaSlider(
                value = value?.toFloat() ?: 0f,
                valueRange = 0f..sliderMax,
                modifier = Modifier.weight(1f),
                onValueChange = { updateValue(it.toInt()) },
            )
            Box {
                Text(
                    modifier = Modifier
                        .alpha(if ((value ?: 0) > sliderMax) 0f else 1f)
                        .padding(8.dp),
                    text = strings.widgets.latency.sliderMax,
                    style = MaterialTheme.typography.bodySmall,
                    color = tokens.fg2,
                )
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.Center)
                        .alpha(if ((value ?: 0) > sliderMax) 1f else 0f),
                    imageVector = Icons.Clock,
                    contentDescription = null,
                    tint = tokens.fg2,
                )
            }
        }
        Spacer(modifier = Modifier.size(4.dp))
    }
}

@Composable
private fun SquareIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val tokens = LocalMockzillaTokens.current
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(tokens.bg3)
            .border(width = 1.dp, color = tokens.line1, shape = RoundedCornerShape(6.dp)),
        content = content,
    )
}

@Preview
@Composable
private fun ResponseLatencyCardPreview() = PreviewSurface {
    ResponseLatencyCard(initialValue = 150, onChange = {}, onReset = {})
}

@Preview
@Composable
private fun ResponseLatencyCardDarkPreview() = PreviewSurface(darkTheme = true) {
    ResponseLatencyCard(initialValue = 3000, onChange = {}, onReset = {})
}
