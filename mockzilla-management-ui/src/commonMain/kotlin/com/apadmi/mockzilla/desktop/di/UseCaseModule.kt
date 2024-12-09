package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorService
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorServiceImpl
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCase
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.ZeroConfSdkWrapper
import com.apadmi.mockzilla.desktop.engine.connection.isLocalIpAddress
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.desktop.engine.device.MonitorLogsUseCaseImpl
import com.apadmi.mockzilla.lib.config.ZeroConfConfig

import org.koin.core.module.Module
import org.koin.dsl.module

import java.net.NetworkInterface

import kotlinx.coroutines.GlobalScope

internal fun useCaseModule(): Module = module {
    single<MetaDataUseCase> { MetaDataUseCaseImpl(get()) }
    single<MonitorLogsUseCase> { MonitorLogsUseCaseImpl(get(), get()) }
    single<AdbConnectorService> { AdbConnectorServiceImpl }
    single { ZeroConfSdkWrapper(ZeroConfConfig.serviceType + ".local.", GlobalScope) }
    single<DeviceDetectionUseCase> {
        DeviceDetectionUseCaseImpl(
            isLocalIpAddress = { address ->
                NetworkInterface.getNetworkInterfaces().isLocalIpAddress(address)
            },
            adbConnectorService = get()
        ).also {
            get<ZeroConfSdkWrapper>().setListener(it::onChangedServiceEvent)
        }
    }
}
