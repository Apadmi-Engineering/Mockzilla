package com.apadmi.mockzilla.desktop.di

import com.apadmi.mockzilla.desktop.MockzillaDesktopBuildConfig
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorService
import com.apadmi.mockzilla.desktop.engine.connection.AdbConnectorServiceImpl
import com.apadmi.mockzilla.desktop.engine.connection.AdbEmulatorDiscoveryService
import com.apadmi.mockzilla.desktop.engine.connection.AdbEmulatorDiscoveryServiceImpl
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCase
import com.apadmi.mockzilla.desktop.engine.connection.DeviceDetectionUseCaseImpl
import com.apadmi.mockzilla.desktop.engine.connection.ZeroConfSdkWrapper
import com.apadmi.mockzilla.desktop.engine.connection.isLocalIpAddress
import com.apadmi.mockzilla.desktop.ui.deviceconnection.DeviceConnectionViewModel
import com.apadmi.mockzilla.desktop.ui.devicetabs.DeviceTabsViewModel
import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.di.utils.viewModel
import com.apadmi.mockzilla.ui.utils.MockzillaUiVersion

import org.koin.dsl.module

import java.net.NetworkInterface

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

@OptIn(DelicateCoroutinesApi::class)
fun startDesktopMockzillaKoin() {
    MockzillaUiKoinContext.startMockzillaUiKoinIfNeeded(listOf(module {
        single<AdbConnectorService> { AdbConnectorServiceImpl }
        single<AdbEmulatorDiscoveryService> { AdbEmulatorDiscoveryServiceImpl(get(), get()) }
        single<DeviceDetectionUseCase> {
            DeviceDetectionUseCaseImpl(
                isLocalIpAddress = { address ->
                    NetworkInterface.getNetworkInterfaces().isLocalIpAddress(address)
                },
                adbConnectorService = get()
            ).also { useCase ->
                get<ZeroConfSdkWrapper>().setListener(useCase::onChangedServiceEvent)
                get<AdbEmulatorDiscoveryService>().start(
                    scope = GlobalScope,
                    onEvent = useCase::onChangedServiceEvent
                )
            }
        }
        single { ZeroConfSdkWrapper(ZeroConfConfig.serviceType + ".local.", GlobalScope) }
        single<MockzillaUiVersion> { MockzillaUiVersion(MockzillaDesktopBuildConfig.version) }
        viewModel { DeviceConnectionViewModel(get(), get(), get()) }
        viewModel { DeviceTabsViewModel(get(), get()) }
    }))
}
