package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.ui.engine.maxLatencySliderMs
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.darkSurface
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.utils.minimumTouchTarget

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days

private const val buttonCornerRadiusDark = 4
private const val buttonCornerRadiusLight = 6
private const val msPerSecond = 1000

private val maxLatencyMs = 1.days.inWholeMilliseconds.toInt()

private val sliderMax = maxLatencySliderMs.toFloat()

private object SecondsSuffixTransformation : VisualTransformation {
    private const val suffix = " s"

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = offset.coerceIn(0, text.length)
            override fun transformedToOriginal(offset: Int) = offset.coerceIn(0, text.length)
        }
        return TransformedText(text + AnnotatedString(suffix), offsetMapping)
    }
}

private fun Int.clamped() = min(max(0, this), maxLatencyMs)

private fun Int.msToSecondsText(): String {
    val seconds = this / msPerSecond.toDouble()
    return if (seconds == seconds.toLong().toDouble()) {
        seconds.toLong().toString()
    } else {
        seconds.toString()
    }
}

private fun String.secondsTextToMs(): Int? = toDoubleOrNull()?.let { (it * msPerSecond).roundToInt().clamped() }

private fun String.filterAsDecimal(): String {
    val digitsAndDot = filter { it.isDigit() || it == '.' }
    val firstDotIndex = digitsAndDot.indexOf('.')
    return if (firstDotIndex == -1) {
        digitsAndDot
    } else {
        digitsAndDot.take(firstDotIndex + 1) + digitsAndDot.substring(firstDotIndex + 1).filter { it.isDigit() }
    }
}

private fun String.withCursorAtEnd() = TextFieldValue(text = this, selection = TextRange(length))

@Suppress(
    "MAGIC_NUMBER",
    "LONG_PARAMETER_LIST",
    "FLOAT_IN_ACCURATE_CALCULATIONS"
)
@Composable
internal fun ResponseLatencyCard(
    modifier: Modifier = Modifier,
    initialValue: Int?,
    isOverflowing: Boolean,
    onChange: (Int) -> Unit,
    onReset: () -> Unit,
    showHeader: Boolean = true,
    strings: Strings = LocalStrings.current,
) {
    var value by remember {
        mutableStateOf(initialValue)
    }
    var textValue by remember {
        mutableStateOf((initialValue?.msToSecondsText() ?: "").withCursorAtEnd())
    }
    var lastEmittedValue by remember {
        mutableStateOf(initialValue)
    }

    LaunchedEffect(initialValue) {
        if (initialValue != lastEmittedValue) {
            value = initialValue
            textValue = (initialValue?.msToSecondsText() ?: "").withCursorAtEnd()
            lastEmittedValue = initialValue
        }
    }

    val updateValue = remember(onChange) {
        { it: Int ->
            val clamped = it.clamped()
            value = clamped
            textValue = clamped.msToSecondsText().withCursorAtEnd()
            lastEmittedValue = clamped
            onChange(clamped)
        }
    }

    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalForceDarkMode.current
    val componentShape = if (isDark) RoundedCornerShape(4.dp) else RoundedCornerShape(6.dp)

    Column(
        modifier = modifier
            .fillMaxWidth(),
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

                BaseButton(
                    variant = ButtonVariant.Ghost,
                    size = ButtonSize.Sm,
                    leadingIcon = Icons.Default.Close,
                    label = strings.widgets.latency.clear,
                    onClick = onReset,
                )
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp)
                        .background(
                            color = colorScheme.background,
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
                    BasicTextField(
                        value = textValue,
                        onValueChange = { newValue ->
                            val filtered = newValue.text.filterAsDecimal()
                            textValue = if (filtered == newValue.text) {
                                newValue
                            } else {
                                filtered.withCursorAtEnd()
                            }
                            val parsed = filtered.secondsTextToMs()
                            value = parsed
                            parsed?.let {
                                val clamped = parsed.clamped()
                                lastEmittedValue = clamped
                                onChange(clamped)
                            }
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = LocalMonoFontFamily.current,
                            color = value?.let {
                                colorScheme.warning.primary
                            } ?: colorScheme.onSurfaceMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                        ),
                        cursorBrush = SolidColor(colorScheme.primary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        visualTransformation = SecondsSuffixTransformation,
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (textValue.text.isEmpty()) {
                                    Text(
                                        text = strings.widgets.latency.notSet,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = LocalMonoFontFamily.current,
                                            color = colorScheme.onSurfaceMuted,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                        ),
                                    )
                                }
                                innerTextField()
                            }
                        },
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
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                CustomSlider(
                    value = value?.toFloat() ?: 0f,
                    valueRange = 0f..sliderMax,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = if (isOverflowing) 4.dp else 0.dp),
                    onValueChange = {
                        updateValue(it.toInt())
                    },
                )

                if (isOverflowing) {
                    Box(
                        modifier = Modifier
                            .size(width = 2.dp, height = 14.dp)
                            .background(colorScheme.onSurfaceVariant, RoundedCornerShape(1.dp)),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = strings.widgets.latency.sliderMin,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        fontWeight = FontWeight.Normal,
                    )
                )
                if (isOverflowing) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = strings.widgets.latency.sliderMax,
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp),
                    )
                } else {
                    Text(
                        text = strings.widgets.latency.sliderMax,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            listOf(0, 300, 1000, 3000, 10000, 99000).forEach { ms ->
                val isSelected = value == ms
                val label = when {
                    ms == 0 -> "0"
                    ms < 1000 -> strings.widgets.latency.millisecondLabel(ms)
                    else -> strings.widgets.latency.secondLabel(ms / 1000)
                }

                Box(
                    modifier = Modifier
                        .minimumTouchTarget()
                        .clip(componentShape)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) colorScheme.primary.copy(alpha = 0.5f) else colorScheme.outline,
                            shape = componentShape,
                        )
                        .background(
                            if (isSelected) {
                                colorScheme.primaryContainer
                            } else if (isDark) {
                                colorScheme.surfaceContainer
                            } else {
                                colorScheme.background
                            }
                        )
                        .clickable { updateValue(ms) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = LocalMonoFontFamily.current,
                            color = if (isSelected) colorScheme.primary else colorScheme.onSurfaceMuted,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
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
        isOverflowing = false,
        onChange = {},
        onReset = {},
    )
}
