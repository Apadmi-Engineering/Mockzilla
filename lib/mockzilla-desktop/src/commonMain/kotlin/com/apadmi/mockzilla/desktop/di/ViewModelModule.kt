package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun viewModelModule(): Module = module {
    factory { DeviceConnectionViewModel() }
}
