package com.apadmi.mockzilla.ui.ui.common.components.editor

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.TextRange

// Automatically indents new lines to match the indentation of the line above,
// with an extra level when the previous line opens a block with '{' or '['.
internal class JsonAutoIndentTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        // `selection.collapsed` means it's a cursor (no range selected). We only
        // auto-indent on a plain cursor, and not when it's at the very start.
        val cursorPos = selection.start
        if (!selection.collapsed || cursorPos == 0) {
            return
        }

        // Only act on insertions (e.g. Enter), not deletions (e.g. Backspace). Without
        // this check, backspacing on an empty line lands the cursor after the previous
        // line's '\n', which looks identical to pressing Enter — and the indent gets
        // re-inserted, making the backspace appear to do nothing.
        if (length <= originalText.length) {
            return
        }

        // asCharSequence() returns a live view of the buffer with no full-text copy —
        // TextFieldBuffer doesn't implement CharSequence directly, but this gives us
        // all the standard Kotlin CharSequence extensions (lastIndexOf, substring, get).
        val seq = asCharSequence()

        when (seq[cursorPos - 1]) {
            '\n' -> handleNewLine(seq, cursorPos)
            '}', ']' -> handleCloseBlockOrList(seq, cursorPos)
            else -> {
                // this is a generated else block
            }
        }
    }

    @Suppress("COMMENTED_OUT_CODE")

    private fun TextFieldBuffer.handleCloseBlockOrList(
        seq: CharSequence,
        cursorPos: Int
    ) {
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

    private fun TextFieldBuffer.handleNewLine(
        seq: CharSequence,
        cursorPos: Int
    ) {
        // Find where the previous line started. `cursorPos - 1` is the '\n' we just
        // inserted, so we search backwards from `cursorPos - 2` for the newline that
        // opened that previous line. Adding 1 skips past that opening newline
        // (or lands at 0 if we're on the first line).
        val prevLineStartIndex = seq.lastIndexOf('\n', cursorPos - 2) + 1

        // Scan forward through the previous line to measure its leading whitespace.
        var indentEndIndex = prevLineStartIndex
        while (indentEndIndex < cursorPos - 1 &&
                (seq[indentEndIndex] == ' ' || seq[indentEndIndex] == '\t')
        ) {
            indentEndIndex++
        }

        // The whitespace prefix shared with the previous line (small allocation).
        val baseIndent = seq.substring(prevLineStartIndex, indentEndIndex)

        // Scan backward for the last meaningful character on the previous line,
        // skipping trailing whitespace — avoids creating a trimEnd() substring.
        var lastMeaningfulIndex = cursorPos - 2
        while (lastMeaningfulIndex >= prevLineStartIndex &&
                (seq[lastMeaningfulIndex] == ' ' || seq[lastMeaningfulIndex] == '\t')
        ) {
            lastMeaningfulIndex--
        }
        val prevLineLastChar =
            if (lastMeaningfulIndex >= prevLineStartIndex) seq[lastMeaningfulIndex] else null

        // Add one extra indent level when the previous line opened a JSON block.
        val extraIndent = if (prevLineLastChar == '{' || prevLineLastChar == '[') "  " else ""

        val indentToInsert = baseIndent + extraIndent
        if (indentToInsert.isNotEmpty()) {
            replace(cursorPos, cursorPos, indentToInsert)
            selection = TextRange(cursorPos + indentToInsert.length)
        }
    }
}
