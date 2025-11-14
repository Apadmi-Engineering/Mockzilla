package com.apadmi.mockzilla.ui.ui.common.utils

import com.apadmi.mockzilla.lib.internal.utils.multiPlatformIo
import com.apadmi.mockzilla.ui.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

private const val debounceTime = 600L
internal fun ViewModel.withDebounce(job: Job?, op: suspend () -> Result<Unit>): Job {
    job?.cancel()
    return viewModelScope.launch(Dispatchers.multiPlatformIo) {
        delay(debounceTime)
        yield()
        op()
    }
}
