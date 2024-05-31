package com.apadmi.mockzilla.desktop.ui.components

/**
 * @property name
 * @property group
 * @property styleName
 * @property widthDp
 * @property heightDp
 * @property skip
 * @property defaultStyle
 * @property tags
 * @property extraMetadata
 */
actual annotation class ShowkaseComposable(
    actual val name: String = "",
    actual val group: String = "",
    actual val styleName: String = "",
    actual val widthDp: Int = -1,
    actual val heightDp: Int = -1,
    actual val skip: Boolean = false,
    actual val defaultStyle: Boolean = false,
    actual val tags: Array<String> = [],
    actual val extraMetadata: Array<String> = [],
)
