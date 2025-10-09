package com.apadmi.mockzilla.mobile.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.mobile.ui.utils.startMockzillaMobileUiKoin
import org.koin.dsl.module

import platform.UIKit.*

fun launchManagementUi() {
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: throw IllegalStateException("No root ViewController found, cannot push Mockzilla UI.")
    val vc = createManagementUiViewController {
        root.dismissViewControllerAnimated(true, null)
    }

    root.presentViewController(vc, animated = true, completion = null)
}

fun createManagementUiViewController(onClose: () -> Unit): UIViewController {
    startMockzillaMobileUiKoin(module {
        single { FileIo() }
    })

    return ComposeUIViewController(configure = { enforceStrictPlistSanityCheck = false }) {
        MobileAppRoot(onClose = onClose)
    }
}
