package com.apadmi.mockzilla.ui.ui.common.widgets

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.utils.debounced
import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.clock
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

// Arbitrary max just to stop overflow
private val maxLatencyMs = 1.days.inWholeMilliseconds.toInt()
private fun Int.clamped() = min(max(0, this), maxLatencyMs)
private val sliderMax = 60.seconds.inWholeMilliseconds.toFloat()

@Composable
internal fun ResponseLatencyCard(
    initialValue: Int?,
    onChange: (Int) -> Unit,
    strings: Strings = LocalStrings.current
) {
    var value by remember(initialValue) { mutableStateOf(initialValue) }
    val updateValue = remember(initialValue) {
        { it: Int ->
            value = it.clamped()
            onChange(it.clamped())
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(resource = Res.drawable.clock),
                contentDescription = null
            )
            Text(
                text = strings.widgets.latency.title,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            SquareIconButton(
                enabled = value != null && value != 0,
                onClick = {
                    updateValue((value ?: 0) - 100)
                }) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus")
            }
            Spacer(Modifier.size(12.dp))
            CustomTextField(
                modifier = Modifier.weight(1f),
                value = value?.toString() ?: "",
                placeholder = { Text("Not Set") },
                onValueChange = { updateValue(it.toIntOrNull() ?: 0) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = {
                    if (value != null) {
                        Text(strings.widgets.latency.millisecondLabel)
                    } else {
                        Box(Modifier)
                    }
                })
            Spacer(Modifier.size(12.dp))
            SquareIconButton(
                enabled = value != null && value != maxLatencyMs,
                onClick = {
                    updateValue((value ?: 0) + 100)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.padding(8.dp), text = strings.widgets.latency.sliderMin)
            Slider(
                value?.toFloat() ?: 0f,
                valueRange = 0f..sliderMax,
                modifier = Modifier.weight(1f),
                onValueChange = { updateValue(it.toInt()) }
            )
            Box {
                Text(modifier = Modifier
                    .alpha(if ((value ?: 0) > sliderMax) 0f else 1f)
                    .padding(8.dp), text = strings.widgets.latency.sliderMax)
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                        .alpha(if ((value ?: 0) > sliderMax) 1f else 0f),
                    painter = painterResource(resource = Res.drawable.clock),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun SquareIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) = IconButton(
    onClick = onClick,
    enabled = enabled,
    modifier = Modifier.border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(8.dp)
    ),
    content = content
)

@Preview
@Composable
private fun ResponseLatencyCardPreview() = PreviewSurface {
    ResponseLatencyCard(150, {})
}
