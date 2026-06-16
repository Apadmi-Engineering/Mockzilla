@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.common.components.editor

/**
 * @property startIndex First index of token in html
 * @property endIndex Last index of token in html
 * @property token Token type
 */
internal data class HtmlTokenIndex(
    val startIndex: Int,
    val endIndex: Int,
    val token: HtmlToken?
)

internal enum class HtmlToken {
    @Suppress("COMMENT_WHITE_SPACE")
    AttributeEquals,  // =
    AttributeName,  // identifier for attribute (class, id, href…)
    AttributeValue,  // "..." or '...'
    Bracket,  // < > </ />
    Comment,  // <!-- ... -->
    DocType,  // <!DOCTYPE ...>
    TagName,  // element name after < or </
    ;
}

internal object HtmlTokens {
    private val nextTokenList = listOf(
        "<!--", "<!DOCTYPE", "<!doctype",
        "</", "/>", "<", ">",
        "=", "\"", "'"
    )

    @Suppress("NESTED_BLOCK", "TOO_LONG_FUNCTION")
    fun nextToken(body: String, startIndex: Int): HtmlTokenIndex {
        val match = body.findAnyOf(nextTokenList, startIndex)

        match?.let { (index, token) ->
            // Before handling structural token, return any identifier in the gap first
            if (index > startIndex) {
                identifierAt(body, startIndex)?.let { (identStart, identEnd, htmlToken) ->
                    if (identStart < index) {
                        return HtmlTokenIndex(identStart, identEnd, htmlToken)
                    }
                }
            }

            when (token) {
                "<!--" -> {
                    val end = body.indexOf("-->", index + 4)  // 4 is length of the comment close
                    return if (end == -1) {
                        HtmlTokenIndex(index, body.length, HtmlToken.Comment)
                    } else {
                        HtmlTokenIndex(index, end + 3, HtmlToken.Comment)
                    }
                }
                "<!DOCTYPE", "<!doctype" -> {
                    val end = body.indexOf('>', index)
                    return if (end == -1) {
                        HtmlTokenIndex(index, body.length, HtmlToken.DocType)
                    } else {
                        HtmlTokenIndex(index, end + 1, HtmlToken.DocType)
                    }
                }
                "</" -> return HtmlTokenIndex(index, index + 2, HtmlToken.Bracket)
                "<" -> return HtmlTokenIndex(index, index + 1, HtmlToken.Bracket)
                "/>" -> return HtmlTokenIndex(index, index + 2, HtmlToken.Bracket)
                ">" -> return HtmlTokenIndex(index, index + 1, HtmlToken.Bracket)
                "=" -> return if (isInsideTag(body, index)) {
                    HtmlTokenIndex(index, index + 1, HtmlToken.AttributeEquals)
                } else {
                    HtmlTokenIndex(startIndex, index + 1, null)
                }
                "\"", "'" -> return if (isInsideTag(body, index)) {
                    val closeIdx = body.indexOf(token[0], index + 1).takeIf { it != -1 } ?: body.lastIndex
                    HtmlTokenIndex(index, closeIdx + 1, HtmlToken.AttributeValue)
                } else {
                    HtmlTokenIndex(startIndex, index + 1, null)
                }
                else -> Unit
            }
        }

        // No structural match: check for identifier (tag/attribute name)
        identifierAt(body, startIndex)?.let { (identStart, identEnd, htmlToken) ->
            return HtmlTokenIndex(identStart, identEnd, htmlToken)
        }

        return HtmlTokenIndex(startIndex, startIndex + 1, null)
    }
}

private fun isInsideTag(body: String, index: Int): Boolean {
    for (i in index - 1 downTo 0) {
        when (body[i]) {
            '<' -> return true
            '>' -> return false
            else -> {
                // this is a generated else block
            }
        }
    }
    return false
}

private fun isTagNameContext(body: String, index: Int): Boolean {
    var i = index - 1
    while (i >= 0 && body[i] in " \t\r\n/") {
        i--
    }
    return i >= 0 && body[i] == '<'
}

private fun readIdentifier(body: String, startIndex: Int): Int {
    var i = startIndex
    while (i < body.length && (body[i].isLetterOrDigit() || body[i] == '-' || body[i] == '_' || body[i] == ':')) {
        i++
    }
    return i
}

private fun identifierAt(body: String, startIndex: Int): Triple<Int, Int, HtmlToken>? {
    var i = startIndex
    while (i < body.length && body[i] in " \t\r\n") {
        i++
    }
    if (i >= body.length || (!body[i].isLetter() && body[i] != '_')) {
        return null
    }
    if (!isInsideTag(body, i)) {
        return null
    }
    val endIdx = readIdentifier(body, i)
    val token = if (isTagNameContext(body, i)) HtmlToken.TagName else HtmlToken.AttributeName
    return Triple(i, endIdx, token)
}
