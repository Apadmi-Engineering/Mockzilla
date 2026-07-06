package com.apadmi.mockzilla.ui.engine.find

internal fun findMatches(text: String, query: String): List<IntRange> {
    if (query.isBlank()) {
        return emptyList()
    }
    val result = mutableListOf<IntRange>()
    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase()
    var index = 0
    while (index <= lowerText.length - lowerQuery.length) {
        val found = lowerText.indexOf(lowerQuery, index)
        if (found == -1) {
            break
        }
        result.add(found until found + lowerQuery.length)
        index = found + 1
    }
    return result
}
