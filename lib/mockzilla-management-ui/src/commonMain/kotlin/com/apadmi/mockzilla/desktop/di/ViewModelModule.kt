package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.di.utils.viewModel
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun viewModelModule(): Module = module {
    viewModel { DeviceConnectionViewModel(get(), get()) }
    viewModel { MetaDataWidgetViewModel(get(), get()) }
    viewModel { DeviceTabsViewModel(get())}
}
