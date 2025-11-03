package com.apadmi.mockzilla.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual open class ViewModel actual constructor(scope: CoroutineScope?) {
    actual val viewModelScope: CoroutineScope = scope ?: CoroutineScope(Dispatchers.Default)
}
