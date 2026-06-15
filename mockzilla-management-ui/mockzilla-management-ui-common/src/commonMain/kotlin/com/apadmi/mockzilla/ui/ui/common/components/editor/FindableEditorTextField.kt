@file:Suppress("FILE_NAME_MATCH_CLASS", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.components.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField

import androidx.compose.ui.input.key.isCtrlPressed

private data class FindReplaceState(
    val searchTerm: String = "",
    val replaceTerm: String = "",
    val matches: List<IntRange> = emptyList(),
    val currentMatchIndex: Int = 0,
    val isPanelOpen: Boolean = false,
    val isReplaceMode: Boolean = false,
)

@Composable
internal fun FindableEditorTextField(
    body: String,
    onBodyChange: (String) -> Unit,
    mode: EditorMode,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String,
    parseError: String? = null,
) {
    var state by remember { mutableStateOf(FindReplaceState()) }

    LaunchedEffect(body, state.searchTerm) {
        val newMatches = findMatches(body, state.searchTerm)
        state = state.copy(
            matches = newMatches,
            currentMatchIndex = state.currentMatchIndex.coerceIn(
                0,
                (newMatches.size - 1).coerceAtLeast(0)
            ),
        )
    }

    val colorScheme = MaterialTheme.colorScheme
    val highlightTransformation = remember(
        state.matches,
        state.currentMatchIndex,
        colorScheme.primary,
        colorScheme.onSurface,
    ) {
        if (state.matches.isEmpty() || state.searchTerm.isEmpty()) {
            null
        } else {
            FindHighlightOutputTransformation(
                matches = state.matches,
                currentMatchIndex = state.currentMatchIndex,
                activeColor = colorScheme.primary.copy(alpha = 0.45f),
                inactiveColor = colorScheme.onSurface.copy(alpha = 0.15f),
            )
        }
    }

    fun navigateNext() {
        if (state.matches.isEmpty()) return
        state = state.copy(currentMatchIndex = (state.currentMatchIndex + 1) % state.matches.size)
    }

    fun navigatePrev() {
        if (state.matches.isEmpty()) return
        state = state.copy(
            currentMatchIndex = (state.currentMatchIndex - 1 + state.matches.size) % state.matches.size
        )
    }

    Box(
        modifier = modifier.onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
            val isModifier = event.isMetaPressed || event.isCtrlPressed
            when {
                isModifier && event.key == Key.F -> {
                    state = state.copy(isPanelOpen = true, isReplaceMode = false)
                    true
                }
                isModifier && event.key == Key.R -> {
                    state = state.copy(isPanelOpen = true, isReplaceMode = true)
                    true
                }
                event.key == Key.Escape && state.isPanelOpen -> {
                    state = state.copy(isPanelOpen = false)
                    true
                }
                else -> false
            }
        }
    ) {
        EditorTextField(
            body = body,
            onBodyChange = onBodyChange,
            mode = mode,
            isExpanded = isExpanded,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            parseError = parseError,
            additionalOutputTransformation = highlightTransformation,
        )

        AnimatedVisibility(
            visible = state.isPanelOpen,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            FindReplaceBar(
                state = state,
                onSearchChange = { term -> state = state.copy(searchTerm = term, currentMatchIndex = 0) },
                onReplaceChange = { term -> state = state.copy(replaceTerm = term) },
                onNext = ::navigateNext,
                onPrev = ::navigatePrev,
                onToggleReplaceMode = { state = state.copy(isReplaceMode = !state.isReplaceMode) },
                onClose = { state = state.copy(isPanelOpen = false) },
                onReplace = {
                    if (state.matches.isNotEmpty()) {
                        val match = state.matches[state.currentMatchIndex]
                        val newBody = body.substring(0, match.first) +
                                state.replaceTerm +
                                body.substring(match.last + 1)
                        onBodyChange(newBody)
                    }
                },
                onReplaceAll = {
                    if (state.searchTerm.isNotEmpty()) {
                        onBodyChange(body.replace(state.searchTerm, state.replaceTerm, ignoreCase = true))
                    }
                },
            )
        }
    }
}

@Composable
private fun FindReplaceBar(
    state: FindReplaceState,
    onSearchChange: (String) -> Unit,
    onReplaceChange: (String) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onToggleReplaceMode: () -> Unit,
    onClose: () -> Unit,
    onReplace: () -> Unit,
    onReplaceAll: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val colorScheme = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(bottomStart = 8.dp),
        color = colorScheme.surfaceContainerHigh,
        shadowElevation = 4.dp,
        modifier = Modifier.padding(end = 2.dp, top = 1.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                CustomTextField(
                    value = state.searchTerm,
                    onValueChange = onSearchChange,
                    placeholderText = "Find",
                    singleLine = true,
                    modifier = Modifier
                        .width(180.dp)
                        .focusRequester(focusRequester)
                        .onKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                            when {
                                event.key == Key.Enter && event.isShiftPressed -> { onPrev(); true }
                                event.key == Key.Enter -> { onNext(); true }
                                else -> false
                            }
                        },
                    textStyle = MaterialTheme.typography.bodySmall,
                )

                val matchLabel = when {
                    state.searchTerm.isEmpty() -> ""
                    state.matches.isEmpty() -> "No results"
                    else -> "${state.currentMatchIndex + 1} / ${state.matches.size}"
                }
                if (matchLabel.isNotEmpty()) {
                    Text(
                        text = matchLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onSurfaceVariant,
                    )
                }

                IconButton(onClick = onPrev, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Previous match",
                        modifier = Modifier.size(18.dp),
                    )
                }
                IconButton(onClick = onNext, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Next match",
                        modifier = Modifier.size(18.dp),
                    )
                }
                IconButton(onClick = onToggleReplaceMode, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (state.isReplaceMode) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.Default.KeyboardArrowRight
                        },
                        contentDescription = "Toggle replace",
                        modifier = Modifier.size(18.dp),
                    )
                }
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close find bar",
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            AnimatedVisibility(visible = state.isReplaceMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    CustomTextField(
                        value = state.replaceTerm,
                        onValueChange = onReplaceChange,
                        placeholderText = "Replace",
                        singleLine = true,
                        modifier = Modifier.width(180.dp),
                        textStyle = MaterialTheme.typography.bodySmall,
                    )
                    TextButton(
                        onClick = onReplace,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text("Replace", style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(
                        onClick = onReplaceAll,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text("All", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

internal class CompositeOutputTransformation(
    private val first: OutputTransformation,
    private val second: OutputTransformation,
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        with(first) { transformOutput() }
        with(second) { transformOutput() }
    }
}

private class FindHighlightOutputTransformation(
    private val matches: List<IntRange>,
    private val currentMatchIndex: Int,
    private val activeColor: Color,
    private val inactiveColor: Color,
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        matches.forEachIndexed { i, range ->
            val bg = if (i == currentMatchIndex) activeColor else inactiveColor
            addStyle(SpanStyle(background = bg), range.first, range.last + 1)
        }
    }
}

private fun findMatches(text: String, query: String): List<IntRange> {
    if (query.isBlank()) return emptyList()
    val result = mutableListOf<IntRange>()
    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase()
    var index = 0
    while (index <= lowerText.length - lowerQuery.length) {
        val found = lowerText.indexOf(lowerQuery, index)
        if (found == -1) break
        result.add(found until found + lowerQuery.length)
        index = found + 1
    }
    return result
}
