package com.apadmi.mockzilla.desktop.di.utils

import com.apadmi.mockzilla.desktop.di.useCaseModule
import com.apadmi.mockzilla.desktop.di.viewModelModule
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceManagerImpl
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module

internal fun startMockzillaKoin() = startKoin {
    modules(
        viewModelModule(),
        useCaseModule(),
        module {
            single { MockzillaManagement.create() }
            single { ActiveDeviceManagerImpl(GlobalScope) } binds arrayOf(
                ActiveDeviceMonitor::class,
                ActiveDeviceSelector::class
            )
        }
    )
}
