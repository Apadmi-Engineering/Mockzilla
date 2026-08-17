@file:Suppress("FILE_NAME_MATCH_CLASS", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.components.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.engine.find.FindReplaceState
import com.apadmi.mockzilla.ui.engine.find.findMatches
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomIconButton

@Composable
internal fun FindableEditorTextField(
    textFieldState: TextFieldState,
    mode: EditorMode,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String,
    parseError: String? = null,
) {
    var state by remember { mutableStateOf(FindReplaceState()) }

    LaunchedEffect(textFieldState.text, state.searchTerm) {
        val newMatches = findMatches(textFieldState.text.toString(), state.searchTerm)
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
        state.isPanelOpen,
        colorScheme.primary,
        colorScheme.onSurface,
    ) {
        if (state.matches.isEmpty() || state.searchTerm.isEmpty() || !state.isPanelOpen) {
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

    val navigateNext = {
        if (state.matches.isNotEmpty()) {
            state = state.copy(currentMatchIndex = (state.currentMatchIndex + 1) % state.matches.size)
        }
    }

    val navigatePrev = {
        if (state.matches.isNotEmpty()) {
            state = state.copy(
                currentMatchIndex = (state.currentMatchIndex - 1 + state.matches.size) % state.matches.size
            )
        }
    }

    Box(
        modifier = modifier.onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) {
                return@onKeyEvent false
            }
            val isModifier = event.isMetaPressed || event.isCtrlPressed
            when {
                isModifier && event.key == Key.F -> {
                    val sel = textFieldState.selection
                    val selectedText = if (!sel.collapsed) {
                        textFieldState.text.substring(sel.min, sel.max)
                    } else {
                        ""
                    }
                    state = state.copy(
                        isPanelOpen = true,
                        isReplaceMode = false,
                        searchTerm = if (selectedText.isNotEmpty()) selectedText else state.searchTerm,
                        currentMatchIndex = 0,
                    )
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
            textFieldState = textFieldState,
            mode = mode,
            isExpanded = isExpanded,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            parseError = parseError,
            additionalOutputTransformation = highlightTransformation,
            currentMatch = if (state.isPanelOpen) state.matches.getOrNull(state.currentMatchIndex) else null,
        )

        AnimatedVisibility(
            visible = state.isPanelOpen,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            FindReplaceBar(
                state = state,
                onSearchChange = { term -> state = state.copy(searchTerm = term, currentMatchIndex = 0) },
                onReplaceChange = { term -> state = state.copy(replaceTerm = term) },
                onNext = navigateNext,
                onPrev = navigatePrev,
                onToggleReplaceMode = { state = state.copy(isReplaceMode = !state.isReplaceMode) },
                onClose = { state = state.copy(isPanelOpen = false) },
                onReplace = {
                    if (state.matches.isNotEmpty()) {
                        val match = state.matches[state.currentMatchIndex]
                        val oldBody = textFieldState.text
                        val newBody = oldBody.substring(0, match.first) +
                                state.replaceTerm +
                                oldBody.substring(match.last + 1)
                        textFieldState.edit { replace(0, length, newBody) }
                    }
                },
                onReplaceAll = {
                    if (state.searchTerm.isNotEmpty()) {
                        val oldBody = textFieldState.text.toString()
                        val newBody = oldBody.replace(
                            state.searchTerm, state.replaceTerm, ignoreCase = true
                        )
                        textFieldState.edit { replace(0, length, newBody) }
                    }
                },
            )
        }
    }
}

@Suppress("LONG_METHOD")
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
    val strings = LocalStrings.current.components.findReplace
    val isNoMatch = state.searchTerm.isNotEmpty() && state.matches.isEmpty()

    val chevronRotation by animateFloatAsState(
        targetValue = if (state.isReplaceMode) 90f else 0f,
        animationSpec = tween(150),
        label = "replace_chevron",
    )

    val matchLabel = when {
        state.searchTerm.isEmpty() -> ""
        state.matches.isEmpty() -> strings.noResults
        else -> strings.matchCount(state.currentMatchIndex + 1, state.matches.size)
    }

    val panelShape = RoundedCornerShape(bottomStart = 24.dp, topEnd = 8.dp)
    Surface(
        shape = panelShape,
        color = colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .width(380.dp)
            .border(width = 1.dp, color = colorScheme.outlineVariant, shape = panelShape),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                CustomIconButton(
                    onClick = onToggleReplaceMode,
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    iconTint = colorScheme.onSurfaceVariant,
                    contentDescription = if (state.isReplaceMode) strings.collapseReplaceDescription else strings.expandReplaceDescription,
                    iconSize = 16.dp,
                    iconDecoration = { content -> content(Modifier.rotate(chevronRotation)) }
                )

                CustomTextField(
                    value = state.searchTerm,
                    onValueChange = onSearchChange,
                    placeholderText = strings.findPlaceholder,
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    containerColor = colorScheme.surface,
                    textStyle = MaterialTheme.typography.bodySmall,
                    suffix = if (matchLabel.isNotEmpty()) {
                        {
                            Text(
                                text = matchLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isNoMatch) colorScheme.error else colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier
                        .height(30.dp)
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown) {
                                return@onKeyEvent false
                            }
                            when {
                                event.key == Key.Enter && event.isShiftPressed -> {
                                    onPrev()
                                    true
                                }
                                event.key == Key.Enter -> {
                                    onNext()
                                    true
                                }
                                else -> false
                            }
                        },
                )

                CustomIconButton(
                    onClick = onPrev,
                    imageVector = Icons.Default.KeyboardArrowUp,
                    iconTint = colorScheme.onSurfaceVariant,
                    contentDescription = strings.previousMatchDescription,
                    iconSize = 16.dp,
                )
                CustomIconButton(
                    onClick = onNext,
                    imageVector = Icons.Default.KeyboardArrowDown,
                    iconTint = colorScheme.onSurfaceVariant,
                    contentDescription = strings.nextMatchDescription,
                    iconSize = 16.dp,
                )
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(1.dp)
                        .background(colorScheme.outlineVariant),
                )
                CustomIconButton(
                    onClick = onClose,
                    imageVector = Icons.Default.Close,
                    iconTint = colorScheme.onSurfaceVariant,
                    contentDescription = strings.closeFindBarDescription,
                    iconSize = 14.dp,
                )
            }

            AnimatedVisibility(visible = state.isReplaceMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Spacer(modifier = Modifier.size(24.dp))

                    CustomTextField(
                        value = state.replaceTerm,
                        onValueChange = onReplaceChange,
                        placeholderText = strings.replacePlaceholder,
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        containerColor = colorScheme.surface,
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .height(30.dp)
                            .weight(1f),
                    )

                    CustomButton(
                        label = strings.replaceButton,
                        leadingIcon = Icons.Default.FindReplace,
                        variant = ButtonVariant.Soft,
                        size = ButtonSize.Sm,
                        modifier = Modifier.heightIn(min = 30.dp),
                        onClick = onReplace,
                    )
                    CustomButton(
                        label = strings.replaceAllButton,
                        leadingIcon = Icons.Default.DoneAll,
                        variant = ButtonVariant.Soft,
                        size = ButtonSize.Sm,
                        modifier = Modifier.heightIn(min = 30.dp),
                        onClick = onReplaceAll,
                    )
                }
            }
        }
    }
}
