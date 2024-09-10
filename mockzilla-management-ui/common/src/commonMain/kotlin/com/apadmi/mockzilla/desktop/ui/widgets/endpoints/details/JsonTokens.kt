package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

data class TokenIndex(
    val startIndex: Int,
    val endIndex: Int,
    val token: Token?
)

enum class Token {
    OpenObject, // {
    CloseObject, // }
    OpenArray, // [
    CloseArray, // ]
    BlockComment, // /* .... */
    LineComment, // //
    String, // "foo"
    Boolean, // true
    Null, // null
    Number, // 123.45
    ValueSeparator, // ,
    KeySeparator // :
}

object JsonTokens {
    private val startOfNumberTokens = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "+"
    )

    private val mainTokens = listOf(
        "{",
        "}",
        "[",
        "]",
        "/*",
        "//",
        "\"",
        ",",
        ":",
        "true",
        "false",
        "null"
    )

    private val numberCharacters = startOfNumberTokens + listOf(".", "E", "e")

    private val nextTokenList = mainTokens + startOfNumberTokens

    fun nextToken(body: String, startIndex: Int): TokenIndex {
        val match = body.findAnyOf(
            strings = nextTokenList,
            startIndex = startIndex
        )
        if (match != null) {
            val (index, token) = match
            when (token) {
                "{" -> return TokenIndex(
                    startIndex = index,
                    endIndex = index + 1,
                    token = Token.OpenObject
                )

                "}" -> return TokenIndex(
                    startIndex = index,
                    endIndex = index + 1,
                    token = Token.CloseObject
                )

                "[" -> return TokenIndex(
                    startIndex = index,
                    endIndex = index + 1,
                    token = Token.OpenArray
                )

                "]" -> return TokenIndex(
                    startIndex = index,
                    endIndex = index + 1,
                    token = Token.CloseArray
                )

                "," -> return TokenIndex(
                    startIndex = index,
                    endIndex = index + 1,
                    token = Token.ValueSeparator
                )

                ":" -> return TokenIndex(
                    startIndex = index,
                    endIndex = index + 1,
                    token = Token.KeySeparator
                )
            }
            if (token == "//") {
                // return entire line comment as the token so we don't see tokens in commented
                // characters
                val endOfLine = body.findAnyOf(strings = listOf("\n", "\r"), startIndex = index)
                if (endOfLine != null) {
                    val (endOfLineIndex, _) = endOfLine
                    return TokenIndex(
                        startIndex = index,
                        endIndex = endOfLineIndex + 1,
                        token = Token.LineComment
                    )
                } else {
                    return TokenIndex(
                        startIndex = index,
                        endIndex = body.lastIndex,
                        token = Token.LineComment
                    )
                }
            }
            if (token == "/*") {
                // return entire block comment as the token so we don't see tokens in commented
                // characters
                val endOfBlockComment = body
                    .findAnyOf(strings = listOf("*/"), startIndex = index + 2)
                if (endOfBlockComment != null) {
                    val (blockEndIndex, _) = endOfBlockComment
                    return TokenIndex(
                        startIndex = index,
                        endIndex = blockEndIndex + 2,
                        token = Token.BlockComment
                    )
                } else {
                    return TokenIndex(
                        startIndex = index,
                        endIndex = body.lastIndex,
                        token = Token.BlockComment
                    )
                }
            }
            if (token == "\"") {
                // go to end of string so we don't see tokens inside the quotes
                var i = index + 1
                while (i < body.length) {
                    val substring = body.substring(startIndex = index + 1, endIndex = i)
                    if (substring.endsWith("\"") && !substring.endsWith("\\\"")) {
                        break
                    }
                    i += 1
                }
                return TokenIndex(
                    startIndex = index,
                    endIndex = i,
                    token = Token.String
                )
            }
            if (token == "true") {
                return TokenIndex(startIndex = index, endIndex = index + 4, token = Token.Boolean)
            }
            if (token == "false") {
                return TokenIndex(startIndex = index, endIndex = index + 5, token = Token.Boolean)
            }
            if (token == "null") {
                return TokenIndex(startIndex = index, endIndex = index + 4, token = Token.Null)
            }
            if (startOfNumberTokens.contains(token)) {
                // go to the end of the number tokens so we don't see tokens inside the number
                var i = index + 1
                while (i < body.length) {
                    val char = body[i].toString()
                    if (numberCharacters.contains(char)) {
                        i += 1
                    } else {
                        break
                    }
                }
                return TokenIndex(
                    startIndex = index,
                    endIndex = i,
                    token = Token.Number
                )
            }
        }
        // No match, advance by 1
        return TokenIndex(
            startIndex = startIndex,
            endIndex = startIndex + 1,
            token = null
        )
    }
}

