package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.di.utils.viewModel
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCaseImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun useCaseModule(): Module = module {
    single<MetaDataUseCase> { MetaDataUseCaseImpl(get()) }
}
