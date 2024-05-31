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
expect annotation class ShowkaseComposable(
    val name: String,
    val group: String,
    val styleName: String,
    val widthDp: Int,
    val heightDp: Int,
    val skip: Boolean,
    val defaultStyle: Boolean,
    val tags: Array<String>,
    val extraMetadata: Array<String>,
)
