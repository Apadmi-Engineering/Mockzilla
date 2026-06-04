package com.apadmi.mockzilla.ui.di.utils

import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.di.useCaseModule
import com.apadmi.mockzilla.ui.di.viewModelModule
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceManagerImpl
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.EventBusImpl
import com.apadmi.mockzilla.ui.utils.Platform

import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

object MockzillaUiKoinContext {
    private var isInitialised = false

    @OptIn(DelicateCoroutinesApi::class)
    private val koinApp = koinApplication {
        val mockzillaManagement = MockzillaManagement.constructInstance(config = MockzillaManagement.Config(
            // Bypasses proxy when running on mobile devices since the server is on device
            // going via a proxy can redirect calls to the proxy machine instead of the local device
            // (Notably this is needed for Mockzilla to run on Browserstack)
            disableProxy = Platform.current != Platform.Desktop
        ))
        modules(
            viewModelModule(),
            useCaseModule(),
            module {
                single { mockzillaManagement.metaDataService }
                single { mockzillaManagement.logsService }
                single { mockzillaManagement.endpointsService }
                single { mockzillaManagement.updateService }
                single { mockzillaManagement.cacheClearingService }
                single<EventBus> { EventBusImpl(GlobalScope) }
                single { ActiveDeviceManagerImpl(get(), GlobalScope) } binds arrayOf(
                    ActiveDeviceMonitor::class,
                    ActiveDeviceSelector::class
                )
            }
        )
    }

    val koin get() = koinApp.koin

    fun startMockzillaUiKoinIfNeeded(modules: List<Module>) {
        if (!isInitialised) {
            isInitialised = true
            koinApp.koin.loadModules(modules)
        }
    }
}
