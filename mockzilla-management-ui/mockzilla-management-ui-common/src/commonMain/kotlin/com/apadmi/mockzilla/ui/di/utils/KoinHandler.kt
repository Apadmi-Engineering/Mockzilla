package com.apadmi.mockzilla.ui.di.utils

import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.di.useCaseModule
import com.apadmi.mockzilla.ui.di.viewModelModule
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceManagerImpl
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.EventBusImpl

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
        modules(
            viewModelModule(),
            useCaseModule(),
            module {
                single { MockzillaManagement.instance.metaDataService }
                single { MockzillaManagement.instance.logsService }
                single { MockzillaManagement.instance.endpointsService }
                single { MockzillaManagement.instance.updateService }
                single { MockzillaManagement.instance.cacheClearingService }
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
