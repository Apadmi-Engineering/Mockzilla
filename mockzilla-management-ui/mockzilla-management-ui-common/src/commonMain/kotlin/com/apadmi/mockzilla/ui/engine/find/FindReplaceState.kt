package com.apadmi.mockzilla.ui.engine.find

/**
 * @property searchTerm
 * @property replaceTerm
 * @property matches
 * @property currentMatchIndex
 * @property isPanelOpen
 * @property isReplaceMode
 */
internal data class FindReplaceState(
    val searchTerm: String = "",
    val replaceTerm: String = "",
    val matches: List<IntRange> = emptyList(),
    val currentMatchIndex: Int = 0,
    val isPanelOpen: Boolean = false,
    val isReplaceMode: Boolean = false,
)
