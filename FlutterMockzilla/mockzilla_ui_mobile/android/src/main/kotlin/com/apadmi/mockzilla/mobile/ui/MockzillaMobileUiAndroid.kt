package com.apadmi.mockzilla.mobile.ui

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log;
import com.apadmi.mockzilla.mobile.ui.launchManagementUi as launchManagementUiKt
import com.apadmi.mockzilla.mobile.ui.MockzillaUiMobileHostApi

class MockzillaUiMobileAndroid(
    private val activityAccessor: () -> Activity?
) : MockzillaUiMobileHostApi {
    val uiThreadHandler = Handler(Looper.getMainLooper())

    override fun launchManagementUi() {
        val activity = activityAccessor?.invoke() ?: return run {
            Log.i("MockzillaMobileUiAndroid", "Failed to find active activity - cannot launch management UI");
        }
        val nativeRuntimeParams = launchManagementUiKt(activity)
    }

    override fun preloadAssets() {
        /* No-Op */
    }
}