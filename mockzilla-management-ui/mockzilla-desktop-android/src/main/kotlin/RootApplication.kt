package com.apadmi.mockzilla

import android.app.Application
import com.apadmi.mockzilla.desktop.di.startDesktopMockzillaKoin

class RootApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startDesktopMockzillaKoin()
    }
}
