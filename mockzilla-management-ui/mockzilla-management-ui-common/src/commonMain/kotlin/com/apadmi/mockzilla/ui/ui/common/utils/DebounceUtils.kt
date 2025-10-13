package com.apadmi.mockzilla.ui.ui.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlin.time.Clock

@Composable
inline fun debounced(crossinline onValueChange: (Long) -> Unit, debounceTime: Long = 1000L): (Long) -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    val onValueChangeLambda: (Long) -> Unit = {
        val now = Clock.System.now().toEpochMilliseconds()
        if (now - lastTimeClicked > debounceTime) {
            onValueChange(it)
        }
        lastTimeClicked = now
    }
    return onValueChangeLambda
}

@Composable
inline fun debounced(crossinline onClick: () -> Unit, debounceTime: Long = 1000L): () -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    val onClickLambda: () -> Unit = {
        val now = Clock.System.now().toEpochMilliseconds()
        if (now - lastTimeClicked > debounceTime) {
            onClick()
        }
        lastTimeClicked = now
    }
    return onClickLambda
}
