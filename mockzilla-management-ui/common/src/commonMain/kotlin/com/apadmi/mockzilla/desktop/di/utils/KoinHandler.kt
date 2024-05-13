package com.apadmi.mockzilla.desktop.di.utils

import com.apadmi.mockzilla.desktop.di.useCaseModule
import com.apadmi.mockzilla.desktop.di.viewModelModule
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceManagerImpl
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector

import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module

import kotlinx.coroutines.GlobalScope

fun startMockzillaKoin() = startKoin {
    modules(
        viewModelModule(),
        useCaseModule(),
        module {
            single { ActiveDeviceManagerImpl(get(), GlobalScope) } binds arrayOf(
                ActiveDeviceMonitor::class,
                ActiveDeviceSelector::class
            )
        }
    )
}.let { Unit }
