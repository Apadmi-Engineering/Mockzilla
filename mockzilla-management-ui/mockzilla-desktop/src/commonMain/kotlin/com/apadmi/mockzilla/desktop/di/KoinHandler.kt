package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorService
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorServiceImpl
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCase
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.ZeroConfSdkWrapper
import com.apadmi.mockzilla.desktop.engine.connection.isLocalIpAddress
import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionViewModel
import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.di.utils.viewModel

import org.koin.dsl.module

import java.net.NetworkInterface

import kotlinx.coroutines.GlobalScope

fun startDesktopMockzillaKoin() {
    MockzillaUiKoinContext.startMockzillaUiKoinIfNeeded(listOf(module {
        single<AdbConnectorService> { AdbConnectorServiceImpl }
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
        single { ZeroConfSdkWrapper(ZeroConfConfig.serviceType + ".local.", GlobalScope) }
        viewModel { DeviceConnectionViewModel(get(), get(), get()) }
        viewModel { DeviceTabsViewModel(get(), get()) }
    }))
}
