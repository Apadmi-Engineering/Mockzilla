@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.i18n.LocalStrings

import com.apadmi.mockzilla.ui.ui.common.assets.DragCorner
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.jsonHighlight
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointBodyVisualTransformation
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.HtmlBodyVisualTransformation

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

private const val editorDefaultHeightDp = 200
private const val syntaxHighlightLineLimit = 500

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
    parseError: String? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val monoFont = LocalMonoFontFamily.current
    val scrollState = rememberScrollState()

    var fieldHeight by remember { mutableStateOf(editorDefaultHeightDp.dp) }
    var lineCount by remember { mutableStateOf(1) }
    val isLargeFile = lineCount > syntaxHighlightLineLimit
    val textFieldState = rememberTextFieldState(body)

    LaunchedEffect(body) {
        if (textFieldState.text.toString() != body) {
            textFieldState.edit {
                replace(0, length, body)
                selection = TextRange(length)
            }
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .drop(1)
            .distinctUntilChanged()
            .collect { onBodyChange(it) }
    }

    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        color = colorScheme.onSurface,
        fontFamily = monoFont,
    )
    val outputTransformation = buildEditorOutputTransformation(mode)

    Column(
        modifier = modifier.border(
            1.dp,
            if (parseError != null) colorScheme.error else colorScheme.outline,
            RoundedCornerShape(8.dp),
        )
    ) {
        EditorContent(
            textFieldState = textFieldState,
            scrollState = scrollState,
            textStyle = textStyle,
            outputTransformation = outputTransformation,
            mode = mode,
            isExpanded = isExpanded,
            fieldHeight = fieldHeight,
            onHeightDrag = { delta -> fieldHeight = (fieldHeight + delta).coerceIn(100.dp, 600.dp) },
            isLargeFile = isLargeFile,
            lineCount = lineCount,
            placeholder = placeholder,
            onLineCountChange = { lineCount = it },
            modifier = if (isExpanded) Modifier.weight(1f).fillMaxWidth() else Modifier,
        )

        if (isLargeFile) {
            Text(
                text = "Syntax highlighting disabled for large files",
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurfaceMuted,
                modifier = Modifier
                    .padding(16.dp),
            )
        }


        AnimatedVisibility(
            visible = parseError != null
        ) {
            EditorErrorBanner(parseError = parseError)
        }
    }
}

@Composable
private fun EditorContent(
    textFieldState: TextFieldState,
    scrollState: ScrollState,
    textStyle: TextStyle,
    outputTransformation: OutputTransformation?,
    mode: EditorMode,
    isExpanded: Boolean,
    fieldHeight: Dp,
    onHeightDrag: (Dp) -> Unit,
    isLargeFile: Boolean,
    lineCount: Int,
    placeholder: String,
    onLineCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer(cacheSize = 64)

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val gutterContentWidthPx = remember(lineCount, textStyle, textMeasurer) {
        textMeasurer.measure("$lineCount", textStyle).size.width
    }
    val gutterWidth = with(density) { gutterContentWidthPx.toDp() + 16.dp }
    val gutterTextStyle = textStyle.copy(color = colorScheme.onSurfaceMuted)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (isExpanded) Modifier.fillMaxHeight() else Modifier.height(fieldHeight))
            .clipToBounds()
            .background(colorScheme.surfaceContainerLowest, RoundedCornerShape(8.dp)),
    ) {
        // Gutter background + Canvas line numbers (fixed, outside TextField scroll)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(gutterWidth)
                .background(colorScheme.surfaceContainerLow, shape = RoundedCornerShape(8.dp))
                .clipToBounds(),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val layout = textLayoutResult ?: return@Canvas
                val scrollOffset = scrollState.value.toFloat()
                val paddingTopPx = with(density) { 12.dp.toPx() }

                for (line in 0 until layout.lineCount) {
                    val lineTopOnScreen = paddingTopPx + layout.getLineTop(line) - scrollOffset
                    val lineBottomOnScreen = paddingTopPx + layout.getLineBottom(line) - scrollOffset
                    if (lineBottomOnScreen < 0f) continue
                    if (lineTopOnScreen > size.height) break

                    val measured = textMeasurer.measure("${line + 1}", gutterTextStyle)
                    val x = size.width - measured.size.width - with(density) { 4.dp.toPx() }
                    val lineH = lineBottomOnScreen - lineTopOnScreen
                    val y = lineTopOnScreen + (lineH - measured.size.height) / 2f
                    drawText(measured, topLeft = Offset(x, y))
                }
            }
        }

        // Placeholder (shown when empty, sits at the same position as text content)
        if (textFieldState.text.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceMuted,
                modifier = Modifier.padding(
                    start = gutterWidth + 4.dp,
                    top = 12.dp,
                ),
            )
        }

        BasicTextField(
            state = textFieldState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = gutterWidth + 6.dp,
                    top = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp,
                )
                .onKeyEvent { event ->
                    if (mode == EditorMode.Json &&
                            event.type == KeyEventType.KeyDown &&
                            event.key == Key.Tab
                    ) {
                        val pos = textFieldState.selection.start
                        textFieldState.edit {
                            replace(pos, pos, "  ")
                            selection = TextRange(pos + 2)
                        }
                        true
                    } else {
                        false
                    }
                },
            textStyle = textStyle,
            cursorBrush = SolidColor(colorScheme.primary),
            scrollState = scrollState,
            lineLimits = TextFieldLineLimits.MultiLine(),
            inputTransformation = if (mode == EditorMode.Json && !isLargeFile) JsonAutoIndentTransformation() else null,
            outputTransformation = if (!isLargeFile) outputTransformation else null,
            onTextLayout = { getResult ->
                getResult()?.let { result ->
                    onLineCountChange(result.lineCount)
                    textLayoutResult = result
                }
            },
        )

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
private fun EditorErrorBanner(
    parseError: String?
) {
    val colorScheme = MaterialTheme.colorScheme
    if (parseError != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colorScheme.errorContainer,
                    RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp),
                )
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = colorScheme.onErrorContainer,
                modifier = Modifier.padding(vertical = 2.dp).size(12.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = LocalStrings.current.widgets.createEditPreset.jsonErrorTitle,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = parseError,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurfaceMuted
                )
            }
        }
    }
}

@Composable
private fun buildEditorOutputTransformation(mode: EditorMode): OutputTransformation? {
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
            EditorMode.PlainText -> null
        }
    }
}

// Automatically indents new lines to match the indentation of the line above,
// with an extra level when the previous line opens a block with '{' or '['.
private class JsonAutoIndentTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        // `selection.collapsed` means it's a cursor (no range selected). We only
        // auto-indent on a plain cursor, and not when it's at the very start.
        val cursorPos = selection.start
        if (!selection.collapsed || cursorPos == 0) return

        // Only act on insertions (e.g. Enter), not deletions (e.g. Backspace). Without
        // this check, backspacing on an empty line lands the cursor after the previous
        // line's '\n', which looks identical to pressing Enter — and the indent gets
        // re-inserted, making the backspace appear to do nothing.
        if (length <= originalText.length) return

        // asCharSequence() returns a live view of the buffer with no full-text copy —
        // TextFieldBuffer doesn't implement CharSequence directly, but this gives us
        // all the standard Kotlin CharSequence extensions (lastIndexOf, substring, get).
        val seq = asCharSequence()

        when (seq[cursorPos - 1]) {
            '\n' -> {
                // Find where the previous line started. `cursorPos - 1` is the '\n' we just
                // inserted, so we search backwards from `cursorPos - 2` for the newline that
                // opened that previous line. Adding 1 skips past that opening newline
                // (or lands at 0 if we're on the first line).
                val prevLineStartIndex = seq.lastIndexOf('\n', cursorPos - 2) + 1

                // Scan forward through the previous line to measure its leading whitespace.
                var indentEndIndex = prevLineStartIndex
                while (indentEndIndex < cursorPos - 1 &&
                        (seq[indentEndIndex] == ' ' || seq[indentEndIndex] == '\t')) {
                    indentEndIndex++
                }

                // The whitespace prefix shared with the previous line (small allocation).
                val baseIndent = seq.substring(prevLineStartIndex, indentEndIndex)

                // Scan backward for the last meaningful character on the previous line,
                // skipping trailing whitespace — avoids creating a trimEnd() substring.
                var lastMeaningfulIndex = cursorPos - 2
                while (lastMeaningfulIndex >= prevLineStartIndex &&
                        (seq[lastMeaningfulIndex] == ' ' || seq[lastMeaningfulIndex] == '\t')) {
                    lastMeaningfulIndex--
                }
                val prevLineLastChar = if (lastMeaningfulIndex >= prevLineStartIndex) seq[lastMeaningfulIndex] else null

                // Add one extra indent level when the previous line opened a JSON block.
                val extraIndent = if (prevLineLastChar == '{' || prevLineLastChar == '[') "  " else ""

                val indentToInsert = baseIndent + extraIndent
                if (indentToInsert.isNotEmpty()) {
                    replace(cursorPos, cursorPos, indentToInsert)
                    selection = TextRange(cursorPos + indentToInsert.length)
                }
            }
            '}', ']' -> {
                // Un-indent when a closing brace/bracket is typed on an otherwise blank line.
                // Only strips whitespace when every character before the closer on this line
                // is whitespace — i.e. the user hasn't started typing content before it.
                val lineStartIndex = seq.lastIndexOf('\n', cursorPos - 2) + 1
                var allWhitespace = lineStartIndex < cursorPos - 1
                for (i in lineStartIndex until cursorPos - 1) {
                    if (seq[i] != ' ' && seq[i] != '\t') {
                        allWhitespace = false
                        break
                    }
                }
                if (allWhitespace) {
                    val toRemove = (cursorPos - 1 - lineStartIndex).coerceAtMost(2)
                    replace(lineStartIndex, lineStartIndex + toRemove, "")
                    selection = TextRange(cursorPos - toRemove)
                }
            }
        }
    }
}
