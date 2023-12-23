package com.apadmi.mockzilla.desktop.viewmodel

import kotlinx.coroutines.CoroutineScope

expect open class ViewModel(
    scope: CoroutineScope? = null
) {
    val viewModelScope: CoroutineScope
}
