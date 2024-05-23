package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorUseCase
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCase
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.jmdns.JmdnsWrapperImpl
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
    single<DeviceDetectionUseCase> { DeviceDetectionUseCaseImpl(JmdnsWrapperImpl()) }
}
