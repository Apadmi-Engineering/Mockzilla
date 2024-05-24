package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.engine.adb.AdbConnectorUseCase
import com.apadmi.mockzilla.desktop.engine.adb.AdbConnectorUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCaseImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun useCaseModule(): Module = module {
    single<MetaDataUseCase> { MetaDataUseCaseImpl(get()) }
    single<MonitorLogsUseCase> { MonitorLogsUseCaseImpl(get()) }
    single<AdbConnectorUseCase> { AdbConnectorUseCaseImpl }
}
