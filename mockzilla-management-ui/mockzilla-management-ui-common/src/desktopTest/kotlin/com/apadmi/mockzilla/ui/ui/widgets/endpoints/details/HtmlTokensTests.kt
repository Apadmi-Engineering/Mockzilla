package com.apadmi.mockzilla.ui.ui.widgets.endpoints.details

import com.apadmi.mockzilla.ui.ui.common.components.editor.HtmlToken
import com.apadmi.mockzilla.ui.ui.common.components.editor.HtmlTokenIndex
import com.apadmi.mockzilla.ui.ui.common.components.editor.HtmlTokens
import kotlin.test.Test
import kotlin.test.assertEquals

class HtmlTokensTests {
    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `nextToken tokenises the entire html input`() {
        // <div class="foo">Hello <!-- comment --> <br/></div>
        // 0         1         2         3         4         5
        // 0123456789012345678901234567890123456789012345678901
        val html = """<div class="foo">Hello <!-- comment --> <br/></div>"""

        var cursor = 0
        val tokens = mutableListOf<HtmlTokenIndex>()
        while (cursor < html.length) {
            val token = HtmlTokens.nextToken(html, cursor)
            cursor = token.endIndex
            tokens.add(token)
        }

        assertEquals(
            listOf(
                HtmlTokenIndex(startIndex = 0, endIndex = 1, token = HtmlToken.Bracket),  // <
                HtmlTokenIndex(startIndex = 1, endIndex = 4, token = HtmlToken.TagName),  // div
                HtmlTokenIndex(startIndex = 5, endIndex = 10, token = HtmlToken.AttributeName),  // class
                HtmlTokenIndex(startIndex = 10, endIndex = 11, token = HtmlToken.AttributeEquals),  // =
                HtmlTokenIndex(startIndex = 11, endIndex = 16, token = HtmlToken.AttributeValue),  // "foo"
                HtmlTokenIndex(startIndex = 16, endIndex = 17, token = HtmlToken.Bracket),  // >
                HtmlTokenIndex(startIndex = 23, endIndex = 39, token = HtmlToken.Comment),  // <!-- comment -->
                HtmlTokenIndex(startIndex = 40, endIndex = 41, token = HtmlToken.Bracket),  // <
                HtmlTokenIndex(startIndex = 41, endIndex = 43, token = HtmlToken.TagName),  // br
                HtmlTokenIndex(startIndex = 43, endIndex = 45, token = HtmlToken.Bracket),  // />
                HtmlTokenIndex(startIndex = 45, endIndex = 47, token = HtmlToken.Bracket),  // </
                HtmlTokenIndex(startIndex = 47, endIndex = 50, token = HtmlToken.TagName),  // div
                HtmlTokenIndex(startIndex = 50, endIndex = 51, token = HtmlToken.Bracket),  // >
            ),
            tokens
        )
    }
}
