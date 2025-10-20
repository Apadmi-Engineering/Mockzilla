package com.apadmi.mockzilla.ui.di

import com.apadmi.mockzilla.ui.di.utils.viewModel
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidgetViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.misccontrols.MiscControlsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.MonitorLogsViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun viewModelModule(): Module = module {
    viewModel { MetaDataWidgetViewModel(get(), get()) }
    viewModel { MonitorLogsViewModel(get(), get()) }
    viewModel { MonitorLogDetailsViewModel() }
    viewModel { params -> EndpointsViewModel(params.get(), get(), get(), get()) }
    viewModel { params -> EndpointDetailsViewModel(params.getOrNull(), params.get(), get(), get(), get(), get()) }
    viewModel { params -> MiscControlsViewModel(params.getOrNull(), get(), get()) }
    viewModel { AppRootViewModel(get(), get()) }
}
