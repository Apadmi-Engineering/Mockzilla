package com.apadmi.mockzilla.mobile.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.mobile.ui.utils.startMockzillaMobileUiKoin
import org.koin.dsl.module

import platform.UIKit.*

fun launchManagementUi() {
    startMockzillaMobileUiKoin(module {
        single { FileIo() }
    })

    val root = UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: throw IllegalStateException("No root ViewController found, cannot push mockzilla UI.")
    val controller = ComposeUIViewController(configure = { enforceStrictPlistSanityCheck = false }) {
        MobileAppRoot(onClose = {
            root.dismissViewControllerAnimated(true) { }
        })
    }

    root.presentViewController(controller, animated = true, completion = null)
}
