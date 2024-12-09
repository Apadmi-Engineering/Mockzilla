package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.di.utils.viewModel
import com.apadmi.mockzilla.desktop.ui.AppRootViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection.DeviceConnectionViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.EndpointDetailsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.misccontrols.MiscControlsViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.MonitorLogsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun viewModelModule(): Module = module {
    viewModel { DeviceConnectionViewModel(get(), get(), get()) }
    viewModel { MetaDataWidgetViewModel(get(), get()) }
    viewModel { DeviceTabsViewModel(get(), get()) }
    viewModel { MonitorLogsViewModel(get(), get()) }
    viewModel { params -> EndpointsViewModel(params.get(), get(), get(), get()) }
    viewModel { params -> EndpointDetailsViewModel(params.getOrNull(), params.get(), get(), get(), get(), get()) }
    viewModel { params -> MiscControlsViewModel(params.getOrNull(), get(), get()) }
    viewModel { AppRootViewModel(get(), get()) }
}
