package com.apadmi.mockzilla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.mobile.ui.MobileAppRoot
import com.apadmi.mockzilla.mobile.ui.startMockzillaMobileUiKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class MockzillaManagementSdkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startMockzillaMobileUiKoin(module {
            single { FileIo(applicationContext.cacheDir) }
        })

        setContent {
            CompositionLocalProvider {
                MobileAppRoot(onClose = { finish() })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}
