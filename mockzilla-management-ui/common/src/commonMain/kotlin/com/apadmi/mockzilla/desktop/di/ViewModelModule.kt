package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.di.utils.viewModel
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.EndpointDetailsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.wrapper.MiddlePaneWrapperViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.MonitorLogsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun viewModelModule(): Module = module {
    viewModel { DeviceConnectionViewModel(get(), get(), get()) }
    viewModel { MetaDataWidgetViewModel(get(), get()) }
    viewModel { DeviceTabsViewModel(get(), get()) }
    viewModel { MiddlePaneWrapperViewModel(get()) }
    viewModel { MonitorLogsViewModel(get(), get()) }
    viewModel { EndpointsViewModel(get(), get()) }
    viewModel { EndpointDetailsViewModel(get(), get(), get()) }
}
