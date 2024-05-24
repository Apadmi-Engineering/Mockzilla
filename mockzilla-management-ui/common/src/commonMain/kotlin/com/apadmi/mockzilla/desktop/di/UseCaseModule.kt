package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorUseCase
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCase
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.jmdns.JmdnsWrapper
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCaseImpl
import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import org.koin.core.module.Module
import org.koin.dsl.module
import java.net.InetAddress

internal fun useCaseModule(): Module = module {
    single<MetaDataUseCase> { MetaDataUseCaseImpl(get()) }
    single<MonitorLogsUseCase> { MonitorLogsUseCaseImpl(get()) }
    single<AdbConnectorUseCase> { AdbConnectorUseCaseImpl }
    single { JmdnsWrapper(ZeroConfConfig.serviceType + ".local.") }
    single<DeviceDetectionUseCase> {
        DeviceDetectionUseCaseImpl({ InetAddress.getLocalHost().hostAddress }, get()).also {
            get<JmdnsWrapper>().setListener(it::onChangedServiceEvent)
        }
    }
}
