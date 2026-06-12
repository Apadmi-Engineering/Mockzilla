@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.assets.DragCorner
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.jsonHighlight
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointBodyVisualTransformation
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.HtmlBodyVisualTransformation

private const val editorDefaultHeightDp = 200

internal enum class EditorMode {
    Html, Json, PlainText
}
@Composable
internal fun EditorTextField(
    body: String,
    onBodyChange: (String) -> Unit,
    mode: EditorMode,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String,
    isError: Boolean = false,
    parseError: String? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val monoFont = LocalMonoFontFamily.current
    val scrollState = rememberScrollState()

    var fieldHeight by remember { mutableStateOf(editorDefaultHeightDp.dp) }
    var fieldValue by remember { mutableStateOf(TextFieldValue(body)) }

    LaunchedEffect(body) {
        if (fieldValue.text != body) {
            fieldValue = TextFieldValue(body, selection = TextRange(body.length))
        }
    }

    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = colorScheme.onSurface,
        fontFamily = monoFont,
    )
    val visualTransformation = buildEditorVisualTransformation(mode)

    Column(modifier = modifier) {
        EditorContent(
            fieldValue = fieldValue,
            onFieldValueChange = { newValue ->
                val processed = if (mode == EditorMode.Json) processJsonInput(newValue, fieldValue) else newValue
                fieldValue = processed
                onBodyChange(processed.text)
            },
            scrollState = scrollState,
            textStyle = textStyle,
            visualTransformation = visualTransformation,
            mode = mode,
            isExpanded = isExpanded,
            fieldHeight = fieldHeight,
            onHeightDrag = { delta -> fieldHeight = (fieldHeight + delta).coerceIn(100.dp, 600.dp) },
            isError = isError,
            placeholder = placeholder,
            modifier = if (isExpanded) Modifier.weight(1f).fillMaxWidth() else Modifier,
        )
        EditorErrorBanner(isError = isError, parseError = parseError)
    }
}

@Composable
private fun EditorContent(
    fieldValue: TextFieldValue,
    onFieldValueChange: (TextFieldValue) -> Unit,
    scrollState: ScrollState,
    textStyle: TextStyle,
    visualTransformation: VisualTransformation,
    mode: EditorMode,
    isExpanded: Boolean,
    fieldHeight: Dp,
    onHeightDrag: (Dp) -> Unit,
    isError: Boolean,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val density = LocalDensity.current

    var lineCount by remember { mutableStateOf(1) }
    var gutterWidthPx by remember { mutableStateOf(0) }
    val gutterWidth = with(density) { gutterWidthPx.toDp() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (isExpanded) Modifier.fillMaxHeight() else Modifier.height(fieldHeight))
            .clipToBounds()
            .background(colorScheme.surfaceContainerLowest, RoundedCornerShape(8.dp))
            .border(
                1.dp,
                if (isError) colorScheme.error else colorScheme.outline,
                RoundedCornerShape(8.dp),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(gutterWidth + 8.dp)
                .background(colorScheme.surfaceContainerLow),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(start = 8.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            LineNumbersGutter(
                lineCount = lineCount,
                textStyle = textStyle,
                onWidthChange = { gutterWidthPx = it },
            )
            Box(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                BasicTextField(
                    value = fieldValue,
                    onValueChange = onFieldValueChange,
                    visualTransformation = visualTransformation,
                    cursorBrush = SolidColor(colorScheme.primary),
                    onTextLayout = { result -> lineCount = result.lineCount },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { event ->
                            if (mode == EditorMode.Json &&
                                    event.type == KeyEventType.KeyDown &&
                                    event.key == Key.Tab
                            ) {
                                val pos = fieldValue.selection.start
                                val newText =
                                    fieldValue.text.substring(0, pos) + "  " + fieldValue.text.substring(pos)
                                onFieldValueChange(TextFieldValue(newText, selection = TextRange(pos + 2)))
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = textStyle,
                    decorationBox = { innerTextField ->
                        Box {
                            if (fieldValue.text.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorScheme.onSurfaceMuted,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }
        }

        PlatformVerticalScrollbar(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 2.dp, top = 4.dp, bottom = 4.dp),
        )

        if (!isExpanded) {
            Icon(
                imageVector = Icons.DragCorner,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(16.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            val delta = with(density) { dragAmount.y.toDp() }
                            onHeightDrag(delta)
                        }
                    },
            )
        }
    }
}

@Composable
private fun LineNumbersGutter(
    lineCount: Int,
    textStyle: TextStyle,
    onWidthChange: (Int) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .widthIn(min = 20.dp)
            .onSizeChanged { onWidthChange(it.width) },
        horizontalAlignment = Alignment.End,
    ) {
        repeat(lineCount) { i ->
            Text(
                text = "${i + 1}",
                style = textStyle,
                color = colorScheme.onSurfaceMuted,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
private fun EditorErrorBanner(
    isError: Boolean,
    parseError: String?,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .height(44.dp),
    ) {
        if (isError && parseError != null) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.errorContainer, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = colorScheme.onErrorContainer,
                    modifier = Modifier.size(12.dp),
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = parseError,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = parseError,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onErrorContainer.copy(alpha = 0.75f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun buildEditorVisualTransformation(mode: EditorMode): VisualTransformation {
    val colorScheme = MaterialTheme.colorScheme
    val highlight = colorScheme.jsonHighlight
    return remember(mode, highlight, colorScheme.onSurface, colorScheme.onSurfaceVariant) {
        when (mode) {
            EditorMode.Json -> EndpointBodyVisualTransformation(
                comment = SpanStyle(color = colorScheme.onSurface.copy(alpha = 0.5f)),
                brace = SpanStyle(color = colorScheme.onSurfaceVariant),
                comma = SpanStyle(color = colorScheme.onSurfaceVariant),
                colon = SpanStyle(color = colorScheme.onSurfaceVariant),
                key = SpanStyle(color = highlight.keyColor),
                string = SpanStyle(color = highlight.stringColor),
                keyword = SpanStyle(color = highlight.boolColor),
                number = SpanStyle(color = highlight.numberColor),
                default = SpanStyle(color = colorScheme.onSurface),
            )
            EditorMode.Html -> HtmlBodyVisualTransformation(
                bracket = SpanStyle(color = colorScheme.onSurfaceVariant),
                tagName = SpanStyle(color = highlight.keyColor),
                attributeName = SpanStyle(color = highlight.stringColor),
                attributeValue = SpanStyle(color = highlight.numberColor),
                comment = SpanStyle(color = colorScheme.onSurface.copy(alpha = 0.5f)),
                default = SpanStyle(color = colorScheme.onSurface),
            )
            EditorMode.PlainText -> VisualTransformation.None
        }
    }
}

private fun processJsonInput(
    newValue: TextFieldValue,
    oldValue: TextFieldValue,
): TextFieldValue {
    val newText = newValue.text
    val cursor = newValue.selection.start
    if (newValue.selection.collapsed &&
            newText.length == oldValue.text.length + 1 &&
            cursor > 0 &&
            newText[cursor - 1] == '\n'
    ) {
        val prevLineStart = newText.lastIndexOf('\n', cursor - 2) + 1
        var i = prevLineStart
        while (i < cursor - 1 && (newText[i] == ' ' || newText[i] == '\t')) {
            i++
        }
        val indent = newText.substring(prevLineStart, i)
        val prevLineContent = newText.substring(prevLineStart, cursor - 1).trimEnd()
        val extra = if (prevLineContent.lastOrNull() in listOf('{', '[')) "  " else ""
        val insert = indent + extra
        val adjusted = newText.substring(0, cursor) + insert + newText.substring(cursor)
        return TextFieldValue(adjusted, selection = TextRange(cursor + insert.length))
    }
    return newValue
}
