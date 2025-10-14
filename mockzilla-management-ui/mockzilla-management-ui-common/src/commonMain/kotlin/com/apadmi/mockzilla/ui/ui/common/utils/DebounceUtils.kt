package com.apadmi.mockzilla.ui.ui.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.apadmi.mockzilla.ui.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.time.Clock

@Composable
inline fun debounced(crossinline onValueChange: (Int) -> Unit, debounceTime: Int = 1000): (Int) -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    val onValueChangeLambda: (Int) -> Unit = {
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

internal fun ViewModel.withDebounce(job: Job?, op: suspend () -> Result<Unit>): Job {
    job?.cancel()
    return viewModelScope.launch(Dispatchers.IO) {
        delay(600)
        yield()
        op()
    }
}