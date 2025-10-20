package com.apadmi.mockzilla.ui.di

import com.apadmi.mockzilla.ui.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.ui.engine.device.MetaDataUseCaseImpl
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCaseImpl

import org.koin.core.module.Module
import org.koin.dsl.module

internal fun useCaseModule(): Module = module {
    single<MetaDataUseCase> { MetaDataUseCaseImpl(get()) }
    single<MonitorLogsUseCase> { MonitorLogsUseCaseImpl(get(), get()) }
}
